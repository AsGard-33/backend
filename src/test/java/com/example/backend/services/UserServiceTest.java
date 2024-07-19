package com.example.backend.services;

import com.example.backend.domain.entity.User;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("password");
    }

    @Test
    public void testFindById() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertEquals(user, foundUser);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindByIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findById(1L));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User userDetails = new User();
        userDetails.setUsername("updateduser");
        userDetails.setEmail("updated@example.com");
        userDetails.setPassword("updatedpassword");

        User updatedUser = userService.updateUser(1L, userDetails);

        assertEquals(userDetails.getUsername(), updatedUser.getUsername());
        assertEquals(userDetails.getEmail(), updatedUser.getEmail());
        assertEquals(userDetails.getPassword(), updatedUser.getPassword());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testDeleteUserSuccess() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        boolean isDeleted = userService.deleteUser("testuser", "test@example.com", "password");

        assertEquals(true, isDeleted);
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("password", "password");
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUserFailDueToPasswordMismatch() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        boolean isDeleted = userService.deleteUser("testuser", "test@example.com", "wrongpassword");

        assertEquals(false, isDeleted);
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).matches("wrongpassword", "password");
        verify(userRepository, times(0)).delete(user);
    }

    @Test
    public void testDeleteUserFailDueToUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        boolean isDeleted = userService.deleteUser("testuser", "test@example.com", "password");

        assertEquals(false, isDeleted);
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(0)).matches(anyString(), anyString());
        verify(userRepository, times(0)).delete(user);
    }


}
