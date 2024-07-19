package com.example.backend.controllers;

import com.example.backend.domain.dto.MessageDTO;
import com.example.backend.domain.entity.Message;
import com.example.backend.domain.entity.User;
import com.example.backend.services.MessageService;
import com.example.backend.utils.MessageConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @Mock
    private MessageConverter messageConverter;

    @InjectMocks
    private MessageController messageController;

    private User sender;
    private User recipient;
    private Message message;
    private MessageDTO messageDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        sender = new User();
        sender.setId(1L);
        sender.setUsername("Sender");

        recipient = new User();
        recipient.setId(2L);
        recipient.setUsername("Recipient");

        message = new Message();
        message.setId(1L);
        message.setContent("Hello");
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setConversationId(1L);

        messageDTO = new MessageDTO();
        messageDTO.setId(1L);
        messageDTO.setContent("Hello");
        messageDTO.setSenderId(sender.getId());
        messageDTO.setRecipientId(recipient.getId());
        messageDTO.setConversationId(1L);
    }

    @Test
    public void testCreateMessage() {
        when(messageConverter.toEntity(any(MessageDTO.class))).thenReturn(message);
        when(messageService.saveMessage(any(Message.class))).thenReturn(message);
        when(messageConverter.toDTO(any(Message.class))).thenReturn(messageDTO);

        ResponseEntity<MessageDTO> response = messageController.createMessage(messageDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(messageDTO, response.getBody());
    }

    @Test
    public void testGetMessagesBySender() {
        when(messageService.findBySenderId(anyLong())).thenReturn(Arrays.asList(message));
        when(messageConverter.toDTO(any(Message.class))).thenReturn(messageDTO);

        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesBySender(sender.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(messageDTO, response.getBody().get(0));
    }

    @Test
    public void testGetMessagesByRecipient() {
        when(messageService.findByRecipientId(anyLong())).thenReturn(Arrays.asList(message));
        when(messageConverter.toDTO(any(Message.class))).thenReturn(messageDTO);

        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesByRecipient(recipient.getId());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(messageDTO, response.getBody().get(0));
    }

    @Test
    public void testGetMessagesByConversation() {
        when(messageService.findByConversationId(anyLong())).thenReturn(Arrays.asList(message));
        when(messageConverter.toDTO(any(Message.class))).thenReturn(messageDTO);

        ResponseEntity<List<MessageDTO>> response = messageController.getMessagesByConversation(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(messageDTO, response.getBody().get(0));
    }

    @Test
    public void testDeleteMessage() {
        ResponseEntity<Void> response = messageController.deleteMessage(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
