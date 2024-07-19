package com.example.backend.services;

import com.example.backend.domain.dto.FriendDTO;
import com.example.backend.domain.entity.Friend;
import com.example.backend.domain.entity.User;
import com.example.backend.repositories.FriendRepository;
import com.example.backend.repositories.UserRepository;
import com.example.backend.utils.FriendConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendService {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendConverter friendConverter;

    public FriendDTO addFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new RuntimeException("Friend not found"));

        Friend friendRelation = new Friend(user, friend);
        Friend savedFriend = friendRepository.save(friendRelation);

        return friendConverter.toDTO(savedFriend);
    }

    public List<FriendDTO> getFriends(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<Friend> friends = friendRepository.findByUser(user);

        return friends.stream()
                .map(friendConverter::toDTO)
                .collect(Collectors.toList());
    }

    public void removeFriend(Long userId, Long friendId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        User friend = userRepository.findById(friendId).orElseThrow(() -> new RuntimeException("Friend not found"));

        Friend friendRelation = friendRepository.findByUserAndFriend(user, friend).orElseThrow(() -> new RuntimeException("Friend relation not found"));
        friendRepository.delete(friendRelation);
    }
}
