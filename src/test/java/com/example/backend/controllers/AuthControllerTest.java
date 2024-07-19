package com.example.backend.controllers;

import com.example.backend.domain.dto.LoginDTO;
import com.example.backend.domain.dto.LoginResponseDTO;
import com.example.backend.domain.dto.RegisterDTO;
import com.example.backend.domain.entity.User;
import com.example.backend.services.AuthService;
import com.example.backend.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private HttpServletResponse httpServletResponse;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() {
        RegisterDTO registerDTO = new RegisterDTO();
        registerDTO.setUsername("newuser");
        registerDTO.setEmail("newuser@example.com");
        registerDTO.setPassword("password");

        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(authService.register(any(User.class))).thenReturn(user);
        when(authService.login(anyString(), anyString())).thenReturn("jwtToken");

        ResponseEntity<User> response = authController.registerUser(registerDTO, httpServletResponse);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("newuser", response.getBody().getUsername());
        assertEquals("newuser@example.com", response.getBody().getEmail());
        verify(authService).register(any(User.class));
        verify(httpServletResponse).addCookie(any(Cookie.class));
    }

    @Test
    public void testLogin() {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setUsername("existinguser");
        loginDTO.setPassword("password");

        String expectedToken = "jwtToken";
        String refreshToken = "refreshToken";
        when(authService.login(loginDTO.getUsername(), loginDTO.getPassword())).thenReturn(expectedToken);
        when(authService.generateRefreshToken(loginDTO.getUsername())).thenReturn(refreshToken);

        ResponseEntity<LoginResponseDTO> response = authController.login(loginDTO, httpServletResponse);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expectedToken, response.getBody().getAccess_token());
        verify(authService).login(loginDTO.getUsername(), loginDTO.getPassword());
        verify(authService).generateRefreshToken(loginDTO.getUsername());
        verify(httpServletResponse, times(2)).addCookie(any(Cookie.class));
    }


    @Test
    public void testLogout() {
        authController.logout(httpServletResponse);
        verify(httpServletResponse, times(1)).addCookie(any(Cookie.class));
    }
}
