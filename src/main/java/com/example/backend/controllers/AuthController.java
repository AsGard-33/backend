package com.example.backend.controllers;

import com.example.backend.domain.dto.LoginDTO;
import com.example.backend.domain.dto.LoginResponseDTO;
import com.example.backend.domain.dto.RegisterDTO;
import com.example.backend.domain.dto.UserDTO;
import com.example.backend.domain.entity.User;
import com.example.backend.services.AuthService;
import com.example.backend.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterDTO registerDTO, HttpServletResponse response) {
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());

        User registeredUser = authService.register(user);
        String jwtToken = authService.login(registerDTO.getUsername(), registerDTO.getPassword());

        Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60); // 1 день
        response.addCookie(jwtCookie);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        String jwtToken = authService.login(loginDTO.getUsername(), loginDTO.getPassword());

        Cookie jwtCookie = new Cookie("jwtToken", jwtToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(24 * 60 * 60); // 1 день
        response.addCookie(jwtCookie);

        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(jwtToken, null);
        return ResponseEntity.ok(loginResponseDTO);
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwtToken", null);
        jwtCookie.setPath("/");
        jwtCookie.setHttpOnly(true);
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            logger.error("Authentication is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            logger.error("Principal is not an instance of UserDetails");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String username = ((UserDetails) principal).getUsername();
        if (username == null) {
            logger.error("Username is null");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserDTO user = userService.findByUsername(username);
        if (user == null) {
            logger.error("User not found for username: " + username);
            return ResponseEntity.notFound().build();
        }

        UserDTO userDTO = new UserDTO(user.getId(), user.getUsername(), user.getEmail());
        return ResponseEntity.ok(userDTO);
    }
}

