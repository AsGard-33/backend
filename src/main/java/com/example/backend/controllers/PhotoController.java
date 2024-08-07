package com.example.backend.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.domain.dto.PhotoDTO;
import com.example.backend.domain.entity.Photo;
import com.example.backend.services.PhotoService;
import com.example.backend.utils.PhotoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/photos")
@Validated
public class PhotoController {

    @Autowired
    private PhotoService photoService;

    @Autowired
    private PhotoConverter photoConverter;

    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping(path = "/upload", consumes = "multipart/form-data")
    public ResponseEntity<PhotoDTO> uploadPhoto(
            @RequestParam("title") String title,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("description") String description,
            @RequestParam("userId") Long userId) {
        try {
            String url = savePhoto(photo);

            PhotoDTO photoDTO = new PhotoDTO();
            photoDTO.setTitle(title);
            photoDTO.setUrl(url);
            photoDTO.setDescription(description);
            photoDTO.setUserId(userId);

            Photo photoEntity = photoConverter.toEntity(photoDTO);
            Photo savedPhoto = photoService.savePhoto(photoEntity);
            PhotoDTO savedPhotoDTO = photoConverter.toDTO(savedPhoto);
            return ResponseEntity.ok(savedPhotoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String savePhoto(MultipartFile photo) {
        String fileName = System.currentTimeMillis() + "_" + photo.getOriginalFilename();
        File uploadDirFile = new File(uploadDir);
        if (!uploadDirFile.exists()) {
            uploadDirFile.mkdirs();
        }
        File file = new File(uploadDirFile, fileName);
        try {
            photo.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file due to IOException", e);
        }
        return "/uploads/" + fileName; // Return URL relative to the static resources path
    }

    @PostMapping(path = "/upload-url", consumes = "application/json")
    public ResponseEntity<PhotoDTO> uploadPhotoByUrl(@RequestBody PhotoDTO photoDTO) {
        try {
            Photo photoEntity = photoConverter.toEntity(photoDTO);
            Photo savedPhoto = photoService.savePhoto(photoEntity);
            PhotoDTO savedPhotoDTO = photoConverter.toDTO(savedPhoto);
            return ResponseEntity.ok(savedPhotoDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PhotoDTO>> getAllPhotosByUser(@PathVariable Long userId) {
        List<Photo> photos = photoService.findByUserId(userId);
        List<PhotoDTO> photoDTOs = photos.stream().map(photoConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(photoDTOs);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PhotoDTO>> getAllPhotos() {
        List<Photo> photos = photoService.findAllPhotos();
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