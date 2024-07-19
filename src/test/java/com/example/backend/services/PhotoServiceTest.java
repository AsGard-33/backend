package com.example.backend.services;

import com.example.backend.domain.entity.Photo;
import com.example.backend.repositories.PhotoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class PhotoServiceTest {

    @Mock
    private PhotoRepository photoRepository;

    @InjectMocks
    private PhotoService photoService;

    private Photo photo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        photo = new Photo();
        photo.setId(1L);
        photo.setUrl("http://example.com/photo.jpg");
        photo.setDescription("Test photo");
        photo.setTitle("Test title");
    }

    @Test
    public void testSavePhoto() {
        when(photoRepository.save(any(Photo.class))).thenReturn(photo);

        Photo savedPhoto = photoService.savePhoto(photo);

        assertEquals(photo, savedPhoto);
        verify(photoRepository, times(1)).save(photo);
    }

    @Test
    public void testFindByUserId() {
        when(photoRepository.findByUserId(anyLong())).thenReturn(Arrays.asList(photo));

        List<Photo> photos = photoService.findByUserId(1L);

        assertEquals(1, photos.size());
        assertEquals(photo, photos.get(0));
        verify(photoRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testFindPhotosByTitle() {
        when(photoRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(Arrays.asList(photo));

        List<Photo> photos = photoService.findPhotosByTitle("Test title");

        assertEquals(1, photos.size());
        assertEquals(photo, photos.get(0));
        verify(photoRepository, times(1)).findByTitleContainingIgnoreCase("Test title");
    }

    @Test
    public void testDeletePhoto() {
        photoService.deletePhoto(1L);

        verify(photoRepository, times(1)).deleteById(1L);
    }
}
