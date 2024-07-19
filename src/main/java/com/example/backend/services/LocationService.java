package com.example.backend.services;

import com.example.backend.domain.entity.Location;
import com.example.backend.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public Location findById(Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    public List<Location> findAllLocations() {
        return locationRepository.findAll();
    }

    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    public Location updateLocation(Long id, Location locationDetails) {
        Location location = findById(id);
        if (location == null) {
            throw new RuntimeException("Location not found");
        }
        location.setTitle(locationDetails.getTitle());
        location.setDescription(locationDetails.getDescription());
        location.setImage(locationDetails.getImage());
        location.setCoordinates(locationDetails.getCoordinates());
        return locationRepository.save(location);
    }

    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    public List<Location> findLocationsByTitle(String title) {
        return locationRepository.findByTitleContainingIgnoreCase(title);
    }
}
