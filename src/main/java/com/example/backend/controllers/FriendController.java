package com.example.backend.controllers;

import com.example.backend.domain.dto.FriendDTO;
import com.example.backend.services.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friends")
@Validated
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("/{userId}/{friendId}")
    public ResponseEntity<FriendDTO> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        FriendDTO addedFriend = friendService.addFriend(userId, friendId);
        return ResponseEntity.ok(addedFriend);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<FriendDTO>> getFriends(@PathVariable Long userId) {
        List<FriendDTO> friends = friendService.getFriends(userId);
        return ResponseEntity.ok(friends);
    }

    @DeleteMapping("/{userId}/{friendId}")
    public ResponseEntity<Void> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        friendService.removeFriend(userId, friendId);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/all")
    public ResponseEntity<List<FriendDTO>> getAllFriends() {
        List<FriendDTO> friends = friendService.getAllFriends();
        return ResponseEntity.ok(friends);
    }
}
