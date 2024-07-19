package com.example.backend.controllers;

import com.example.backend.domain.dto.PhotoDTO;
import com.example.backend.domain.entity.Photo;
import com.example.backend.services.PhotoService;
import com.example.backend.utils.PhotoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PhotoControllerTest {

    @Mock
    private PhotoService photoService;

    @Mock
    private PhotoConverter photoConverter;

    @InjectMocks
    private PhotoController photoController;

    private Photo photo;
    private PhotoDTO photoDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        photo = new Photo();
        photo.setId(1L);
        photo.setUrl("http://example.com/photo.jpg");
        photo.setDescription("Test photo");
        photo.setTitle("Test title");

        photoDTO = new PhotoDTO();
        photoDTO.setId(1L);
        photoDTO.setUrl("http://example.com/photo.jpg");
        photoDTO.setDescription("Test photo");
        photoDTO.setTitle("Test title");
    }

    @Test
    public void testUploadPhoto() {
        when(photoConverter.toEntity(any(PhotoDTO.class))).thenReturn(photo);
        when(photoService.savePhoto(any(Photo.class))).thenReturn(photo);
        when(photoConverter.toDTO(any(Photo.class))).thenReturn(photoDTO);

        ResponseEntity<PhotoDTO> response = photoController.uploadPhoto(photoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(photoDTO, response.getBody());
        verify(photoService, times(1)).savePhoto(photo);
        verify(photoConverter, times(1)).toEntity(photoDTO);
        verify(photoConverter, times(1)).toDTO(photo);
    }

    @Test
    public void testGetAllPhotosByUser() {
        when(photoService.findByUserId(anyLong())).thenReturn(Arrays.asList(photo));
        when(photoConverter.toDTO(any(Photo.class))).thenReturn(photoDTO);

        ResponseEntity<List<PhotoDTO>> response = photoController.getAllPhotosByUser(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(photoDTO, response.getBody().get(0));
        verify(photoService, times(1)).findByUserId(1L);
        verify(photoConverter, times(1)).toDTO(photo);
    }

    @Test
    public void testDeletePhoto() {
        ResponseEntity<Void> response = photoController.deletePhoto(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(photoService, times(1)).deletePhoto(1L);
    }

    @Test
    public void testSearchPhotosByTitle() {
        when(photoService.findPhotosByTitle(anyString())).thenReturn(Arrays.asList(photo));
        when(photoConverter.toDTO(any(Photo.class))).thenReturn(photoDTO);

        ResponseEntity<List<PhotoDTO>> response = photoController.searchPhotosByTitle("Test title");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(photoDTO, response.getBody().get(0));
        verify(photoService, times(1)).findPhotosByTitle("Test title");
        verify(photoConverter, times(1)).toDTO(photo);
    }
}
