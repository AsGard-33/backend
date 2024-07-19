package com.example.backend.controllers;

import com.example.backend.domain.dto.PhotoDTO;
import com.example.backend.domain.entity.Photo;
import com.example.backend.services.PhotoService;
import com.example.backend.utils.PhotoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/photos")
@Validated
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoConverter photoConverter;

    @PostMapping
    public ResponseEntity<PhotoDTO> uploadPhoto(@RequestBody PhotoDTO photoDTO) {
        Photo photo = photoConverter.toEntity(photoDTO);
        Photo savedPhoto = photoService.savePhoto(photo);
        PhotoDTO savedPhotoDTO = photoConverter.toDTO(savedPhoto);
        return ResponseEntity.ok(savedPhotoDTO);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PhotoDTO>> getAllPhotosByUser(@PathVariable Long userId) {
        List<Photo> photos = photoService.findByUserId(userId);
        List<PhotoDTO> photoDTOs = photos.stream().map(photoConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(photoDTOs);
    }

    @DeleteMapping("/{photoId}")
    public ResponseEntity<Void> deletePhoto(@PathVariable Long photoId) {
        photoService.deletePhoto(photoId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<PhotoDTO>> searchPhotosByTitle(@PathVariable String title) {
        List<Photo> photos = photoService.findPhotosByTitle(title);
        List<PhotoDTO> photoDTOs = photos.stream().map(photoConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(photoDTOs);
    }
}
