package com.animefan.rest;

import com.animefan.model.Discussion;
import com.animefan.model.Message;
import com.animefan.model.User;
import com.animefan.service.DiscussionService;
import com.animefan.service.MessageService;
import com.animefan.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Slf4j
public class MessageRestController {

    private final MessageService messageService;
    private final DiscussionService discussionService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 🔄 Get all messages for a discussion (for initial load)
     */
    @GetMapping("/discussion/{discussionId}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long discussionId) {
        log.info("📨 Fetching messages for discussion ID: {}", discussionId);
        List<Message> messages = messageService.findByDiscussionIdOrdered(discussionId);
        return ResponseEntity.ok(messages);
    }

    /**
     * 💬 [Optional] REST fallback if you want REST API to send messages (not WebSocket)
     * Use a different endpoint than WebSocket to avoid conflict.
     */
    @PostMapping("/send/{discussionId}")
    public ResponseEntity<?> sendMessageViaRest(@PathVariable Long discussionId, @RequestBody Message message) {
        log.info("📬 REST sendMessage invoked for discussion ID: {}", discussionId);

        Discussion discussion = discussionService.getById(discussionId);
        if (discussion == null) {
            log.warn("⚠️ Discussion not found: {}", discussionId);
            return ResponseEntity.badRequest().body("Invalid discussion ID");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        User user = userService.findByUsername(auth.getName());
        if (user == null) {
            return ResponseEntity.status(401).body("User not found");
        }

        message.setUser(user);
        message.setDiscussion(discussion);
        message.setTimestamp(LocalDateTime.now());

        Message saved = messageService.save(message);

        // Also send via WebSocket so it appears in real time
        messagingTemplate.convertAndSend("/topic/discussion." + discussionId, saved);
        return ResponseEntity.ok(saved);
    }
}
