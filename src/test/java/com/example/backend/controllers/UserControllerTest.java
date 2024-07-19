package com.example.backend.controllers;

import com.example.backend.domain.dto.UserDTO;
import com.example.backend.domain.dto.UserUpdateDTO;
import com.example.backend.domain.entity.User;
import com.example.backend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("existingUser");
        user.setEmail("user@example.com");
        user.setPassword("hashedPassword");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("existingUser");
        userDTO.setEmail("user@example.com");
    }

    @Test
    void testGetUserById() {
        when(userService.findById(anyLong())).thenReturn(user);
        ResponseEntity<UserDTO> response = userController.getUserById(1L);
        assertEquals(200, response.getStatusCodeValue());
        // Использование AssertJ для сравнения объектов по полям
        assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(userDTO);
        verify(userService).findById(1L);
    }

    @Test
    void testUpdateUser() {
        UserUpdateDTO updateDTO = new UserUpdateDTO();
        updateDTO.setUsername("updatedUser");
        updateDTO.setEmail("update@example.com");
        updateDTO.setPassword("newPassword");

        User updatedUser = new User();
        updatedUser.setId(1L);
        updatedUser.setUsername(updateDTO.getUsername());
        updatedUser.setEmail(updateDTO.getEmail());
        updatedUser.setPassword("encodedPassword");

        when(passwordEncoder.encode(updateDTO.getPassword())).thenReturn("encodedPassword");
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(updatedUser);

        ResponseEntity<User> response = userController.updateUser(1L, updateDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUser, response.getBody());
        verify(userService).updateUser(eq(1L), any(User.class));
    }

    @Test
    void testAddFavoriteLocation() {
        when(userService.addFavoriteLocation(anyLong(), anyLong())).thenReturn(user);

        ResponseEntity<User> response = userController.addFavoriteLocation(1L, 100L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(user, response.getBody());
        verify(userService).addFavoriteLocation(1L, 100L);
    }

    @Test
    void testDeleteUser() {
        when(userService.deleteUser(anyString(), anyString(), anyString())).thenReturn(true);

        ResponseEntity<Void> response = userController.deleteUser("existingUser", "user@example.com", "password");

        assertEquals(200, response.getStatusCodeValue());
        verify(userService).deleteUser("existingUser", "user@example.com", "password");
    }

    @Test
    void testDeleteUserFail() {
        when(userService.deleteUser(anyString(), anyString(), anyString())).thenReturn(false);

        ResponseEntity<Void> response = userController.deleteUser("existingUser", "user@example.com", "wrongPassword");

        assertEquals(400, response.getStatusCodeValue());
        verify(userService).deleteUser("existingUser", "user@example.com", "wrongPassword");
    }
}
