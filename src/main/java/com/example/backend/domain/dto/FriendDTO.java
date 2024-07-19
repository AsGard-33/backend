package com.example.backend.domain.dto;

import java.util.Objects;

public class FriendDTO {

    private Long id;
    private UserDTO user;
    private UserDTO friend;

    public FriendDTO() {}

    public FriendDTO(Long id, UserDTO user, UserDTO friend) {
        this.id = id;
        this.user = user;
        this.friend = friend;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public UserDTO getFriend() {
        return friend;
    }

    public void setFriend(UserDTO friend) {
        this.friend = friend;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendDTO friendDTO = (FriendDTO) o;
        return Objects.equals(id, friendDTO.id) &&
                Objects.equals(user, friendDTO.user) &&
                Objects.equals(friend, friendDTO.friend);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, friend);
    }
}
