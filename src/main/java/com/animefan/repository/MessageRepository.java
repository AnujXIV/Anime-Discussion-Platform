package com.animefan.repository;

import com.animefan.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByDiscussion_IdOrderByTimestampAsc(Long discussionId);
}
