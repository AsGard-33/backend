package com.example.backend.controllers;

import com.example.backend.domain.dto.LocationDTO;
import com.example.backend.domain.entity.Location;
import com.example.backend.services.LocationService;
import com.example.backend.utils.LocationConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/locations")
@Validated
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private LocationConverter locationConverter;

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        Location location = locationService.findById(id);
        if (location == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locationConverter.toDTO(location));
    }

    @GetMapping("/")
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        List<Location> locations = locationService.findAllLocations();
        List<LocationDTO> locationDTOs = locations.stream().map(locationConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(locationDTOs);
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) {
        Location location = locationConverter.toEntity(locationDTO);
        Location savedLocation = locationService.saveLocation(location);
        return ResponseEntity.ok(locationConverter.toDTO(savedLocation));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Long id, @RequestBody LocationDTO locationDTO) {
        Location locationDetails = locationConverter.toEntity(locationDTO);
        Location updatedLocation = locationService.updateLocation(id, locationDetails);
        return ResponseEntity.ok(locationConverter.toDTO(updatedLocation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<LocationDTO>> getLocationByTitle(@RequestParam String title) {
        List<Location> locations = locationService.findLocationsByTitle(title);
        List<LocationDTO> locationDTOs = locations.stream().map(locationConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(locationDTOs);
    }
}
