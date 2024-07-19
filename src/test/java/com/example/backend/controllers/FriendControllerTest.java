package com.example.backend.controllers;

import com.example.backend.domain.dto.FriendDTO;
import com.example.backend.domain.dto.UserDTO;
import com.example.backend.services.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class FriendControllerTest {

    @Mock
    private FriendService friendService;

    @InjectMocks
    private FriendController friendController;

    private UserDTO userDTO;
    private UserDTO friendUserDTO;
    private FriendDTO friendDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userDTO = new UserDTO(1L, "alex", "test@test.com");
        friendUserDTO = new UserDTO(2L, "john", "john@test.com");

        friendDTO = new FriendDTO(1L, userDTO, friendUserDTO);
    }

    @Test
    void testAddFriend() {
        when(friendService.addFriend(anyLong(), anyLong())).thenReturn(friendDTO);

        ResponseEntity<FriendDTO> response = friendController.addFriend(1L, 2L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(friendDTO, response.getBody());
        verify(friendService).addFriend(1L, 2L);
    }

    @Test
    void testGetFriends() {
        List<FriendDTO> friends = Arrays.asList(friendDTO);
        when(friendService.getFriends(anyLong())).thenReturn(friends);

        ResponseEntity<List<FriendDTO>> response = friendController.getFriends(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(friends, response.getBody());
        verify(friendService).getFriends(1L);
    }

    @Test
    void testRemoveFriend() {
        doNothing().when(friendService).removeFriend(anyLong(), anyLong());

        ResponseEntity<Void> response = friendController.removeFriend(1L, 2L);

        assertEquals(200, response.getStatusCodeValue());
        verify(friendService).removeFriend(1L, 2L);
    }
}
