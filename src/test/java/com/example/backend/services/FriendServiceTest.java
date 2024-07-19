package com.example.backend.services;

import com.example.backend.domain.dto.FriendDTO;
import com.example.backend.domain.dto.UserDTO;
import com.example.backend.domain.entity.Friend;
import com.example.backend.domain.entity.User;
import com.example.backend.repositories.FriendRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.utils.FriendConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FriendServiceTest {

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendConverter friendConverter;

    @InjectMocks
    private FriendService friendService;

    private User user;
    private User friendUser;
    private Friend friend;
    private FriendDTO friendDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("alex");
        user.setEmail("test@test.com");

        friendUser = new User();
        friendUser.setId(2L);
        friendUser.setUsername("john");
        friendUser.setEmail("john@test.com");

        friend = new Friend();
        friend.setUser(user);
        friend.setFriend(friendUser);

        friendDTO = new FriendDTO(1L, new UserDTO(user.getId(), user.getUsername(), user.getEmail()),
                new UserDTO(friendUser.getId(), friendUser.getUsername(), friendUser.getEmail()));
    }

    @Test
    void testAddFriend() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friendUser));
        when(friendRepository.save(any(Friend.class))).thenReturn(friend);
        when(friendConverter.toDTO(any(Friend.class))).thenReturn(friendDTO);

        FriendDTO addedFriend = friendService.addFriend(1L, 2L);

        assertEquals(friendDTO, addedFriend);
        verify(friendRepository).save(any(Friend.class));
    }

    @Test
    void testAddFriend_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            friendService.addFriend(1L, 2L);
        });

        assertEquals("User not found", exception.getMessage());
        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    void testAddFriend_FriendNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            friendService.addFriend(1L, 2L);
        });

        assertEquals("Friend not found", exception.getMessage());
        verify(friendRepository, never()).save(any(Friend.class));
    }

    @Test
    void testGetFriends() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(friendRepository.findByUser(any(User.class))).thenReturn(Arrays.asList(friend));
        when(friendConverter.toDTO(any(Friend.class))).thenReturn(friendDTO);

        List<FriendDTO> friends = friendService.getFriends(1L);

        assertEquals(1, friends.size());
        assertEquals(friendDTO, friends.get(0));
    }

    @Test
    void testRemoveFriend() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friendUser));
        when(friendRepository.findByUserAndFriend(any(User.class), any(User.class))).thenReturn(Optional.of(friend));

        friendService.removeFriend(1L, 2L);

        verify(friendRepository).delete(any(Friend.class));
    }

    @Test
    void testRemoveFriend_FriendNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(2L)).thenReturn(Optional.of(friendUser));
        when(friendRepository.findByUserAndFriend(any(User.class), any(User.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            friendService.removeFriend(1L, 2L);
        });

        assertEquals("Friend relation not found", exception.getMessage());
        verify(friendRepository, never()).delete(any(Friend.class));
    }
}
