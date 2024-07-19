package com.example.backend.services;

import com.example.backend.domain.entity.Location;
import com.example.backend.repositories.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationService locationService;

    private Location location;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        location = new Location();
        location.setId(1L);
        location.setTitle("Test Location");
        location.setDescription("Description");
        location.setImage("image.jpg");
        location.setCoordinates("coords");
    }

    @Test
    public void testSaveLocation() {
        when(locationRepository.save(any(Location.class))).thenReturn(location);
        Location savedLocation = locationService.saveLocation(location);
        assertNotNull(savedLocation);
        assertEquals(location.getId(), savedLocation.getId());
    }

    @Test
    public void testFindById() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        Location foundLocation = locationService.findById(1L);
        assertNotNull(foundLocation);
        assertEquals(location.getId(), foundLocation.getId());
    }

    @Test
    public void testFindById_NotFound() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());
        Location foundLocation = locationService.findById(1L);
        assertEquals(null, foundLocation);
    }

    @Test
    public void testFindLocationsByTitle() {
        List<Location> locations = Arrays.asList(location);
        when(locationRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(locations);

        List<Location> foundLocations = locationService.findLocationsByTitle("Test");

        assertEquals(locations, foundLocations);
        verify(locationRepository, times(1)).findByTitleContainingIgnoreCase("Test");
    }

    @Test
    public void testUpdateLocation() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.of(location));
        when(locationRepository.save(any(Location.class))).thenReturn(location);

        Location locationDetails = new Location();
        locationDetails.setTitle("Updated Location");
        locationDetails.setDescription("Updated Description");
        locationDetails.setImage("updated.jpg");
        locationDetails.setCoordinates("789,101");

        Location updatedLocation = locationService.updateLocation(1L, locationDetails);

        assertEquals(locationDetails.getTitle(), updatedLocation.getTitle());
        assertEquals(locationDetails.getDescription(), updatedLocation.getDescription());
        assertEquals(locationDetails.getImage(), updatedLocation.getImage());
        assertEquals(locationDetails.getCoordinates(), updatedLocation.getCoordinates());
        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, times(1)).save(location);
    }

    @Test
    public void testUpdateLocation_NotFound() {
        when(locationRepository.findById(anyLong())).thenReturn(Optional.empty());

        Location locationDetails = new Location();
        locationDetails.setTitle("Updated Location");
        locationDetails.setDescription("Updated Description");
        locationDetails.setImage("updated.jpg");
        locationDetails.setCoordinates("789,101");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            locationService.updateLocation(1L, locationDetails);
        });

        assertEquals("Location not found", exception.getMessage());
        verify(locationRepository, times(1)).findById(1L);
        verify(locationRepository, times(0)).save(any(Location.class));}


    @Test
    public void testDeleteLocation() {
        doNothing().when(locationRepository).deleteById(anyLong());
        locationService.deleteLocation(1L);
        verify(locationRepository, times(1)).deleteById(anyLong());
    }
}
