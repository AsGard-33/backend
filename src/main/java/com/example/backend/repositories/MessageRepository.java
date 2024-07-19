package com.example.backend.repositories;

import com.example.backend.domain.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findBySenderId(Long senderId);
    List<Message> findByRecipientId(Long recipientId);
    List<Message> findByConversationId(Long conversationId);
}
