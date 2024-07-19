package com.example.backend.repositories;

import com.example.backend.domain.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByTitleContaining(String title);
    List<Location> findByTitleContainingIgnoreCase(String title);
}
