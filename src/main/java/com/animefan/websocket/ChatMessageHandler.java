package com.animefan.websocket;

import com.animefan.model.Discussion;
import com.animefan.model.Message;
import com.animefan.model.User;
import com.animefan.repository.DiscussionRepository;
import com.animefan.repository.MessageRepository;
import com.animefan.repository.UserRepository;
import com.animefan.websocket.payload.MessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageHandler {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public synchronized MessagePayload sendMessage(@Payload MessagePayload payload) {
        log.info("📩 Incoming message from [{}]: {}", payload.getSender(), payload.getMessage());

        // ✅ Validate input
        if (payload.getSender() == null || payload.getMessage() == null || payload.getDiscussionId() == null) {
            log.warn("❌ Invalid message payload received");
            return null;
        }

        // ✅ Ensure user is authenticated and matches sender
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            log.warn("❌ Unauthenticated user attempted to send a message");
            return null;
        }

        String loggedInUsername = auth.getName();
        if (!loggedInUsername.equals(payload.getSender())) {
            log.warn("⚠️ Payload spoof attempt: logged in [{}], payload sender [{}]", loggedInUsername, payload.getSender());
            return null;
        }

        // ✅ Lookup sender and discussion
        User user = userRepository.findByUsername(payload.getSender()).orElse(null);
        Discussion discussion = discussionRepository.findById(payload.getDiscussionId()).orElse(null);
        if (user == null || discussion == null) {
            log.warn("❌ User or Discussion not found");
            return null;
        }

        // ✅ Save message with timestamp (queue-safe via synchronized method)
        LocalDateTime now = LocalDateTime.now();
        Message message = Message.builder()
                .message(payload.getMessage())
                .user(user)
                .discussion(discussion)
                .timestamp(now)
                .build();

        messageRepository.save(message);
        log.info("✅ Message saved from [{}] in discussion [{}]", user.getUsername(), discussion.getId());

        // ✅ Respond with enriched payload
        payload.setTimestamp(now.toString());
        return payload;
    }
}
