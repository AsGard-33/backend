package com.example.backend.controllers;

import com.example.backend.domain.dto.UserDTO;
import com.example.backend.domain.dto.UserUpdateDTO;
import com.example.backend.domain.entity.User;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.findById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        UserDTO userDTO = convertToDTO(user);
        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody UserUpdateDTO updateDTO) {
        User userDetails = convertToUpdateEntity(updateDTO);
        User updatedUser = userService.updateUser(id, userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    private User convertToUpdateEntity(UserUpdateDTO updateDTO) {
        User user = new User();
        user.setUsername(updateDTO.getUsername());
        user.setEmail(updateDTO.getEmail());
        user.setPassword(passwordEncoder.encode(updateDTO.getPassword())); // Шифрование пароля перед обновлением
        return user;
    }

    @PostMapping("/{userId}/favorites/{locationId}")
    public ResponseEntity<User> addFavoriteLocation(@PathVariable Long userId, @PathVariable Long locationId) {
        User updatedUser = userService.addFavoriteLocation(userId, locationId);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/by-username")
    public ResponseEntity<UserDTO> getUserByUsername(@RequestParam String username) {
        UserDTO user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
        boolean isDeleted = userService.deleteUser(username, email, password);
        if (isDeleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(400).build();
        }
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getEmail());
    }
}
