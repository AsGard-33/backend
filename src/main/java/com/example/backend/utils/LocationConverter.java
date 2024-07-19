package com.example.backend.utils;

import com.example.backend.domain.dto.LocationDTO;
import com.example.backend.domain.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationConverter {

    public LocationDTO toDTO(Location location) {
        return new LocationDTO(
                location.getId(),
                location.getTitle(),
                location.getDescription(),
                location.getImage(),
                location.getCoordinates()
        );
    }

    public Location toEntity(LocationDTO locationDTO) {
        Location location = new Location();
        location.setId(locationDTO.getId());
        location.setTitle(locationDTO.getTitle());
        location.setDescription(locationDTO.getDescription());
        location.setImage(locationDTO.getImage());
        location.setCoordinates(locationDTO.getCoordinates());
        return location;
    }
}
