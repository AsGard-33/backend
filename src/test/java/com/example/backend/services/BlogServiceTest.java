package com.example.backend.services;

import com.example.backend.domain.dto.BlogCreateDTO;
import com.example.backend.domain.entity.Blog;
import com.example.backend.domain.entity.User;
import com.example.backend.repositories.BlogRepository;
import com.example.backend.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BlogServiceTest {

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BlogService blogService;

    private Blog blog;
    private BlogCreateDTO blogCreateDTO;
    private User user;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        blog = new Blog();
        blog.setTitle("Test Title");
        blog.setContent("Test Content");
        blog.setUser(user);

        blogCreateDTO = new BlogCreateDTO();
        blogCreateDTO.setTitle("Test Title");
        blogCreateDTO.setContent("Test Content");
        blogCreateDTO.setUserId(1L);
    }

    @Test
    public void testSaveBlog() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(blogRepository.save(any(Blog.class))).thenReturn(blog);

        Blog savedBlog = blogService.saveBlog(blog);

        assertEquals(blog, savedBlog);
    }

    @Test
    public void testUpdateBlog() {
        Blog updatedBlogDetails = new Blog();
        updatedBlogDetails.setTitle("New Title");
        updatedBlogDetails.setContent("New Content");
        updatedBlogDetails.setUser(user);

        when(blogRepository.findById(anyLong())).thenReturn(Optional.of(blog));
        when(blogRepository.save(any(Blog.class))).thenReturn(updatedBlogDetails);

        Blog updatedBlog = blogService.updateBlog(1L, updatedBlogDetails);

        assertEquals(updatedBlogDetails.getTitle(), updatedBlog.getTitle());
        assertEquals(updatedBlogDetails.getContent(), updatedBlog.getContent());
    }

    @Test
    public void testDeleteBlog() {
        doNothing().when(blogRepository).deleteById(anyLong());

        blogService.deleteBlog(1L);

        verify(blogRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindBlogsByUserId() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(blogRepository.findByUser(any(User.class))).thenReturn(List.of(blog));

        List<Blog> blogs = blogService.findBlogsByUserId(1L);

        assertEquals(1, blogs.size());
        assertEquals(blog, blogs.get(0));
    }

    @Test
    public void testFindBlogsByTitle() {
        when(blogRepository.findByTitleContaining(anyString())).thenReturn(List.of(blog));

        List<Blog> blogs = blogService.findBlogsByTitle("Test");

        assertEquals(1, blogs.size());
        assertEquals(blog, blogs.get(0));
    }

    @Test
    public void testFindById() {
        when(blogRepository.findById(anyLong())).thenReturn(Optional.of(blog));

        Blog foundBlog = blogService.findById(1L);

        assertEquals(blog, foundBlog);
    }

    @Test
    public void testFindByIdNotFound() {
        when(blogRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> blogService.findById(1L));
    }

    @Test
    public void testFindBlogsByUserIdNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> blogService.findBlogsByUserId(1L));
    }
}
