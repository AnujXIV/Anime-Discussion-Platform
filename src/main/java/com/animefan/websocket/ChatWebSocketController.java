package com.animefan.websocket;

import com.animefan.model.Discussion;
import com.animefan.model.Message;
import com.animefan.model.User;
import com.animefan.service.DiscussionService;
import com.animefan.service.MessageService;
import com.animefan.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final DiscussionService discussionService;

    @MessageMapping("/chat.send/{discussionId}")
    public void sendMessage(Message message, @DestinationVariable Long discussionId) {
        Discussion discussion = discussionService.getById(discussionId);
        User user = userService.getById(message.getUser().getId());

        if (discussion != null && user != null) {
            message.setDiscussion(discussion); // ✅ Ensures discussion ID is saved
            message.setUser(user);             // ✅ Sets full user
            message.setTimestamp(LocalDateTime.now());
            messageService.save(message);
            messagingTemplate.convertAndSend("/topic/discussion/" + discussionId, message);
        }
    }

}
