package com.example.backend.utils;

import com.example.backend.domain.dto.PhotoDTO;
import com.example.backend.domain.entity.Photo;
import com.example.backend.domain.entity.User;
import com.example.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhotoConverter {

    @Autowired
    private UserService userService;

    public PhotoDTO toDTO(Photo photo) {
        PhotoDTO photoDTO = new PhotoDTO();
        photoDTO.setId(photo.getId());
        photoDTO.setUrl(photo.getUrl());
        photoDTO.setDescription(photo.getDescription());
        photoDTO.setTitle(photo.getTitle());
        photoDTO.setUserId(photo.getUser().getId());
        return photoDTO;
    }

    public Photo toEntity(PhotoDTO photoDTO) {
        Photo photo = new Photo();
        photo.setId(photoDTO.getId());
        photo.setUrl(photoDTO.getUrl());
        photo.setDescription(photoDTO.getDescription());
        photo.setTitle(photoDTO.getTitle());
        User user = userService.findById(photoDTO.getUserId());
        photo.setUser(user);
        return photo;
    }
}
