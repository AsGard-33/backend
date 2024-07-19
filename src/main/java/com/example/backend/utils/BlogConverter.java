package com.example.backend.utils;

import com.example.backend.domain.dto.BlogCreateDTO;
import com.example.backend.domain.entity.Blog;
import com.example.backend.domain.entity.User;
import com.example.backend.repositories.UserRepository;

public class BlogConverter {
    public static BlogCreateDTO toDTO(Blog blog) {
        BlogCreateDTO dto = new BlogCreateDTO();
        dto.setId(blog.getId());
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setUserId(blog.getUser().getId());
        return dto;
    }

    public static Blog toEntity(BlogCreateDTO dto, UserRepository userRepository) {
        Blog blog = new Blog();
        blog.setId(dto.getId());
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        blog.setUser(user);
        return blog;
    }
}
