package com.animefan.service;

import com.animefan.model.Message;
import com.animefan.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public List<Message> findByDiscussionIdOrdered(Long discussionId) {
        return messageRepository.findByDiscussion_IdOrderByTimestampAsc(discussionId);
    }

    public Message save(Message message) {
        return messageRepository.save(message);
    }
}
