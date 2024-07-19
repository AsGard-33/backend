package com.example.backend.repositories;

import com.example.backend.domain.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    List<Photo> findByUserId(Long userId);
    List<Photo> findByTitleContainingIgnoreCase(String title);
}
