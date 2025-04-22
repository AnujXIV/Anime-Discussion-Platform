package com.animefan.websocket.payload;

import lombok.Data;

@Data
public class MessagePayload {
    private String sender;
    private String message;
    private Long discussionId;
    private String timestamp;
}
