package com.example.backend.controllers;

import com.example.backend.domain.dto.MessageDTO;
import com.example.backend.domain.entity.Message;
import com.example.backend.services.MessageService;
import com.example.backend.utils.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
@Validated
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageConverter messageConverter;

    @PostMapping
    public ResponseEntity<MessageDTO> createMessage(@RequestBody MessageDTO messageDTO) {
        Message message = messageConverter.toEntity(messageDTO);
        Message savedMessage = messageService.saveMessage(message);
        MessageDTO savedMessageDTO = messageConverter.toDTO(savedMessage);
        return ResponseEntity.ok(savedMessageDTO);
    }

    @GetMapping("/sender/{senderId}")
    public ResponseEntity<List<MessageDTO>> getMessagesBySender(@PathVariable Long senderId) {
        List<Message> messages = messageService.findBySenderId(senderId);
        List<MessageDTO> messageDTOs = messages.stream().map(messageConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/recipient/{recipientId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByRecipient(@PathVariable Long recipientId) {
        List<Message> messages = messageService.findByRecipientId(recipientId);
        List<MessageDTO> messageDTOs = messages.stream().map(messageConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByConversation(@PathVariable Long conversationId) {
        List<Message> messages = messageService.findByConversationId(conversationId);
        List<MessageDTO> messageDTOs = messages.stream().map(messageConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(messageDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok().build();
    }
}
