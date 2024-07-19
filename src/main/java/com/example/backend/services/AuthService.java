package com.example.backend.services;

import com.example.backend.domain.entity.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername());
        }

        throw new IllegalArgumentException("Invalid username or password");
    }

    // Генерация JWT токена
    public String generateJwtToken(String username) {
        return jwtUtil.generateToken(username);
    }

    // Генерация рефреш токена
    public String generateRefreshToken(String username) {
        return jwtUtil.generateRefreshToken(username);
    }

    // Валидация рефреш токена
    public boolean validateRefreshToken(String token) {
        // Просто проверяем не истек ли токен, так как имя пользователя не нужно для этой операции
        return jwtUtil.validateToken(token);
    }

    // Извлечение имени пользователя из рефреш токена
    public String extractUsernameFromRefreshToken(String token) {
        return jwtUtil.extractUsername(token);
    }

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        throw new IllegalArgumentException("Invalid username or password");
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }
}
