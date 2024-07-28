package com.example.backend.services;

import com.example.backend.domain.dto.UserDTO;
import com.example.backend.domain.entity.Location;
import com.example.backend.domain.entity.User;
import com.example.backend.exception.ResourceNotFoundException;
import com.example.backend.repositories.LocationRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User updateUser(Long id, User userDetails) {
        User user = findById(id);
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }

    public User addFavoriteLocation(Long userId, Long locationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        user.getFavoriteLocations().add(location);
        return userRepository.save(user);
    }

    public List<UserDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public UserDTO findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
                .orElse(null);
    }

    public boolean deleteUser(String username, String email, String password) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getEmail().equals(email) && passwordEncoder.matches(password, user.getPassword())) {
                userRepository.delete(user);
                return true;
            }
        }
        return false;
    }
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public UserDTO updateAvatar(Long userId, String avatarUrl) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setAvatarUrl(avatarUrl);
        userRepository.save(user);
        return new UserDTO(user);
    }

}
