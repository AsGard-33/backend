package com.example.backend.utils;

import com.example.backend.domain.dto.FriendDTO;
import com.example.backend.domain.dto.UserDTO;
import com.example.backend.domain.entity.Friend;
import org.springframework.stereotype.Component;

@Component
public class FriendConverter {

    public FriendDTO toDTO(Friend friend) {
        UserDTO userDTO = new UserDTO(friend.getUser().getId(), friend.getUser().getUsername(), friend.getUser().getEmail());
        UserDTO friendUserDTO = new UserDTO(friend.getFriend().getId(), friend.getFriend().getUsername(), friend.getFriend().getEmail());
        return new FriendDTO(friend.getId(), userDTO, friendUserDTO);
    }

    public Friend toEntity(FriendDTO friendDTO) {
        // This method can be implemented if needed. For now, we don't need it.
        return null;
    }
}
