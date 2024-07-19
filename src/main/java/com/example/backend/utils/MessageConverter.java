package com.example.backend.utils;

import com.example.backend.domain.dto.MessageDTO;
import com.example.backend.domain.entity.Message;
import com.example.backend.domain.entity.User;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageConverter {

    @Autowired
    private UserService userService;

    public MessageDTO toDTO(Message message) {
        return new MessageDTO(
                message.getId(),
                message.getContent(),
                message.getSender().getId(),
                message.getRecipient().getId(),
                message.getConversationId()
        );
    }

    public Message toEntity(MessageDTO messageDTO) {
        Message message = new Message();
        message.setId(messageDTO.getId());
        message.setContent(messageDTO.getContent());
        User sender = userService.findById(messageDTO.getSenderId());
        User recipient = userService.findById(messageDTO.getRecipientId());
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setConversationId(messageDTO.getConversationId());
        return message;
    }
}
