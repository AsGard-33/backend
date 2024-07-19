package com.example.backend.controllers;

import com.example.backend.domain.dto.BlogCreateDTO;
import com.example.backend.domain.entity.Blog;
import com.example.backend.domain.entity.User;
import com.example.backend.repositories.UserRepository;
import com.example.backend.services.BlogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BlogControllerTest {

    @Mock
    private BlogService blogService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BlogController blogController;

    private Blog blog;
    private BlogCreateDTO blogCreateDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Test Title");
        blog.setContent("Test Content");
        blog.setUser(user);

        blogCreateDTO = new BlogCreateDTO();
        blogCreateDTO.setTitle("Test Title");
        blogCreateDTO.setContent("Test Content");
        blogCreateDTO.setUserId(1L);
    }

    @Test
    void testCreateBlog() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(blogService.saveBlog(any(Blog.class))).thenReturn(blog);

        ResponseEntity<BlogCreateDTO> response = blogController.createBlog(blogCreateDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(blogCreateDTO.getTitle(), response.getBody().getTitle());
        assertEquals(blogCreateDTO.getContent(), response.getBody().getContent());
    }

    @Test
    void testUpdateBlog() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(blogService.updateBlog(anyLong(), any(Blog.class))).thenReturn(blog);

        ResponseEntity<BlogCreateDTO> response = blogController.updateBlog(1L, blogCreateDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(blogCreateDTO.getTitle(), response.getBody().getTitle());
        assertEquals(blogCreateDTO.getContent(), response.getBody().getContent());
    }

    @Test
    void testDeleteBlog() {
        doNothing().when(blogService).deleteBlog(anyLong());

        ResponseEntity<String> response = blogController.deleteBlog(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Blog deleted.", response.getBody());
    }

    @Test
    void testGetBlogsByUserId() {
        List<Blog> blogs = new ArrayList<>();
        blogs.add(blog);

        when(blogService.findBlogsByUserId(anyLong())).thenReturn(blogs);

        ResponseEntity<List<BlogCreateDTO>> response = blogController.getBlogsByUserId(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(blog.getTitle(), response.getBody().get(0).getTitle());
    }

    @Test
    void testSearchBlogsByTitle() {
        List<Blog> blogs = new ArrayList<>();
        blogs.add(blog);

        when(blogService.findBlogsByTitle(anyString())).thenReturn(blogs);

        ResponseEntity<List<BlogCreateDTO>> response = blogController.searchBlogsByTitle("Test");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(blog.getTitle(), response.getBody().get(0).getTitle());
    }

    @Test
    void testGetBlogById() {
        when(blogService.findById(anyLong())).thenReturn(blog);

        ResponseEntity<BlogCreateDTO> response = blogController.getBlogById(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(blog.getTitle(), response.getBody().getTitle());
        assertEquals(blog.getContent(), response.getBody().getContent());
    }
}
