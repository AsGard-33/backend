package com.example.backend.services;

import com.example.backend.domain.entity.Message;
import com.example.backend.domain.entity.User;
import com.example.backend.repositories.MessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @InjectMocks
    private MessageService messageService;

    private User sender;
    private User recipient;
    private Message message;

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
    }

    @Test
    public void testSaveMessage() {
        when(messageRepository.save(any(Message.class))).thenReturn(message);

        Message savedMessage = messageService.saveMessage(message);

        assertEquals(message, savedMessage);
        verify(messageRepository, times(1)).save(message);
    }

    @Test
    public void testFindBySenderId() {
        when(messageRepository.findBySenderId(anyLong())).thenReturn(Arrays.asList(message));

        List<Message> messages = messageService.findBySenderId(sender.getId());

        assertEquals(1, messages.size());
        assertEquals(message, messages.get(0));
        verify(messageRepository, times(1)).findBySenderId(sender.getId());
    }

    @Test
    public void testFindByRecipientId() {
        when(messageRepository.findByRecipientId(anyLong())).thenReturn(Arrays.asList(message));

        List<Message> messages = messageService.findByRecipientId(recipient.getId());

        assertEquals(1, messages.size());
        assertEquals(message, messages.get(0));
        verify(messageRepository, times(1)).findByRecipientId(recipient.getId());
    }

    @Test
    public void testFindByConversationId() {
        when(messageRepository.findByConversationId(anyLong())).thenReturn(Arrays.asList(message));

        List<Message> messages = messageService.findByConversationId(1L);

        assertEquals(1, messages.size());
        assertEquals(message, messages.get(0));
        verify(messageRepository, times(1)).findByConversationId(1L);
    }

    @Test
    public void testDeleteMessage() {
        doNothing().when(messageRepository).deleteById(anyLong());

        messageService.deleteMessage(1L);

        verify(messageRepository, times(1)).deleteById(1L);
    }
}
