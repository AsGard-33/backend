package com.example.backend.services;

import com.example.backend.domain.entity.Photo;
import com.example.backend.repositories.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PhotoService {

    @Autowired
    private PhotoRepository photoRepository;

    public Photo savePhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public List<Photo> findByUserId(Long userId) {
        return photoRepository.findByUserId(userId);
    }

    public List<Photo> findPhotosByTitle(String title) {
        return photoRepository.findByTitleContainingIgnoreCase(title);
    }

    public void deletePhoto(Long photoId) {
        photoRepository.deleteById(photoId);
    }
}
