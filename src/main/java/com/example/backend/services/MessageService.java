package com.example.backend.services;

import com.example.backend.domain.entity.Message;
import com.example.backend.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message saveMessage(Message message) {
        return messageRepository.save(message);
    }

    public List<Message> findBySenderId(Long senderId) {
        return messageRepository.findBySenderId(senderId);
    }

    public List<Message> findByRecipientId(Long recipientId) {
        return messageRepository.findByRecipientId(recipientId);
    }

    public List<Message> findByConversationId(Long conversationId) {
        return messageRepository.findByConversationId(conversationId);
    }

    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}
