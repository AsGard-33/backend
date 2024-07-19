package com.example.backend.controllers;

import com.example.backend.domain.dto.LocationDTO;
import com.example.backend.domain.entity.Location;
import com.example.backend.services.LocationService;
import com.example.backend.utils.LocationConverter;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class LocationControllerTest {

    @Mock
    private LocationService locationService;

    @Mock
    private LocationConverter locationConverter;

    @InjectMocks
    private LocationController locationController;

    private Location location;
    private LocationDTO locationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        location = new Location();
        location.setId(1L);
        location.setTitle("Test Location");
        location.setDescription("Test Description");
        location.setImage("test.jpg");
        location.setCoordinates("123,456");

        locationDTO = new LocationDTO();
        locationDTO.setId(1L);
        locationDTO.setTitle("Test Location");
        locationDTO.setDescription("Test Description");
        locationDTO.setImage("test.jpg");
        locationDTO.setCoordinates("123,456");
    }

    @Test
    public void testGetLocationById() {
        when(locationService.findById(anyLong())).thenReturn(location);
        when(locationConverter.toDTO(any(Location.class))).thenReturn(locationDTO);

        ResponseEntity<LocationDTO> response = locationController.getLocationById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationDTO, response.getBody());
    }

    @Test
    public void testGetLocationById_NotFound() {
        when(locationService.findById(anyLong())).thenReturn(null);

        ResponseEntity<LocationDTO> response = locationController.getLocationById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateLocation() {
        when(locationConverter.toEntity(any(LocationDTO.class))).thenReturn(location);
        when(locationService.saveLocation(any(Location.class))).thenReturn(location);
        when(locationConverter.toDTO(any(Location.class))).thenReturn(locationDTO);

        ResponseEntity<LocationDTO> response = locationController.createLocation(locationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationDTO, response.getBody());
    }

    @Test
    public void testUpdateLocation() {
        when(locationConverter.toEntity(any(LocationDTO.class))).thenReturn(location);
        when(locationService.updateLocation(anyLong(), any(Location.class))).thenReturn(location);
        when(locationConverter.toDTO(any(Location.class))).thenReturn(locationDTO);

        ResponseEntity<LocationDTO> response = locationController.updateLocation(1L, locationDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationDTO, response.getBody());
    }

    @Test
    public void testDeleteLocation() {
        ResponseEntity<Void> response = locationController.deleteLocation(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetLocationByTitle() {
        List<Location> locations = Arrays.asList(location);
        List<LocationDTO> locationDTOs = Arrays.asList(locationDTO);

        when(locationService.findLocationsByTitle(any(String.class))).thenReturn(locations);
        when(locationConverter.toDTO(any(Location.class))).thenReturn(locationDTO);

        ResponseEntity<List<LocationDTO>> response = locationController.getLocationByTitle("Test");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationDTOs, response.getBody());
    }
    @Test
    void testGetAllLocations() {
        List<Location> locations = Arrays.asList(location);
        List<LocationDTO> locationDTOs = Arrays.asList(locationDTO);

        when(locationService.findAllLocations()).thenReturn(locations);
        when(locationConverter.toDTO(any(Location.class))).thenReturn(locationDTO);

        ResponseEntity<List<LocationDTO>> response = locationController.getAllLocations();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(locationDTOs, response.getBody());
    }

    @Test
    public void testUpdateLocation_NotFound() {
        when(locationConverter.toEntity(any(LocationDTO.class))).thenReturn(location);
        when(locationService.updateLocation(anyLong(), any(Location.class))).thenThrow(new RuntimeException("Location not found"));
        when(locationConverter.toDTO(any(Location.class))).thenReturn(locationDTO);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            locationController.updateLocation(1L, locationDTO);
        });

        assertEquals("Location not found", exception.getMessage());
    }
}
