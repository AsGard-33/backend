package com.example.backend.repositories;

import com.example.backend.domain.entity.Blog;
import com.example.backend.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByUser(User user);
    List<Blog> findByTitleContaining(String title);
}
