package com.example.backend.controllers;

import com.example.backend.domain.dto.BlogCreateDTO;
import com.example.backend.domain.entity.Blog;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.BlogService;
import com.example.backend.utils.BlogConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/blogs")
@Validated
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<BlogCreateDTO> createBlog(@RequestBody BlogCreateDTO blogDTO) {
        Blog blog = BlogConverter.toEntity(blogDTO, userRepository);
        Blog savedBlog = blogService.saveBlog(blog);
        return ResponseEntity.ok(BlogConverter.toDTO(savedBlog));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlogCreateDTO> updateBlog(@PathVariable Long id, @RequestBody BlogCreateDTO blogDTO) {
        Blog blogDetails = BlogConverter.toEntity(blogDTO, userRepository);
        Blog updatedBlog = blogService.updateBlog(id, blogDetails);
        return ResponseEntity.ok(BlogConverter.toDTO(updatedBlog));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        return ResponseEntity.ok("Blog deleted.");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BlogCreateDTO>> getBlogsByUserId(@PathVariable Long userId) {
        List<Blog> blogs = blogService.findBlogsByUserId(userId);
        List<BlogCreateDTO> blogDTOs = blogs.stream().map(BlogConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(blogDTOs);
    }

    @GetMapping("/search/{title}")
    public ResponseEntity<List<BlogCreateDTO>> searchBlogsByTitle(@PathVariable String title) {
        List<Blog> blogs = blogService.findBlogsByTitle(title);
        List<BlogCreateDTO> blogDTOs = blogs.stream().map(BlogConverter::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(blogDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlogCreateDTO> getBlogById(@PathVariable Long id) {
        Blog blog = blogService.findById(id);
        return ResponseEntity.ok(BlogConverter.toDTO(blog));
    }
}
