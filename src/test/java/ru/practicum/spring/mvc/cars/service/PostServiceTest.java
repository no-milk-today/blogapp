package ru.practicum.spring.mvc.cars.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.spring.mvc.cars.domain.Post;
import ru.practicum.spring.mvc.cars.dto.PostDto;
import ru.practicum.spring.mvc.cars.mapper.PostFromDtoConverter;
import ru.practicum.spring.mvc.cars.mapper.PostToDtoConverter;
import ru.practicum.spring.mvc.cars.repository.JdbcPostRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private JdbcPostRepository postRepository;

    @Mock
    private PostFromDtoConverter fromDtoConverter;

    @Mock
    private PostToDtoConverter toDtoConverter;

    @InjectMocks
    private PostService underTest;

    @Test
    void testAddPost() {
        var postDto = PostDto.builder()
                .id(1L)
                .title("Test Title")
                .imageUrl("http://example.com/image.jpg")
                .content("Test content")
                .tag("Test")
                .likeCount(100L)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        var post = Post.builder()
                .id(1L)
                .title("Test Title")
                .imageUrl("http://example.com/image.jpg")
                .content("Test content")
                .tag("Test")
                .likeCount(100L)
                .created(postDto.getCreated())
                .updated(postDto.getUpdated())
                .build();

        when(fromDtoConverter.apply(postDto)).thenReturn(post);

        underTest.addPost(postDto);

        verify(postRepository, times(1)).save(post);
    }

    @Test
    void testFindAll() {
        var post1 = Post.builder()
                .id(1L)
                .title("Title 1")
                .imageUrl("http://example.com/1.jpg")
                .content("Content 1")
                .tag("Tag1")
                .likeCount(10L)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        var post2 = Post.builder()
                .id(2L)
                .title("Title 2")
                .imageUrl("http://example.com/2.jpg")
                .content("Content 2")
                .tag("Tag2")
                .likeCount(20L)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        var posts = List.of(post1, post2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Post> postPage = new PageImpl<>(posts, pageable, posts.size());
        when(postRepository.findAll(pageable)).thenReturn(postPage);

        var postDto1 = PostDto.builder()
                .id(1L)
                .title("Title 1")
                .imageUrl("http://example.com/1.jpg")
                .content("Content 1")
                .tag("Tag1")
                .likeCount(10L)
                .created(post1.getCreated())
                .updated(post1.getUpdated())
                .build();

        var postDto2 = PostDto.builder()
                .id(2L)
                .title("Title 2")
                .imageUrl("http://example.com/2.jpg")
                .content("Content 2")
                .tag("Tag2")
                .likeCount(20L)
                .created(post2.getCreated())
                .updated(post2.getUpdated())
                .build();

        when(toDtoConverter.apply(post1)).thenReturn(postDto1);
        when(toDtoConverter.apply(post2)).thenReturn(postDto2);

        var result = underTest.findAll(pageable);

        assertEquals(2, result.getSize());
        assertEquals(postDto1, result.getContent().get(0));
        assertEquals(postDto2, result.getContent().get(1));
    }

    @Test
    void testFindById() {
        var id = 1L;
        var post = Post.builder()
                .id(id)
                .title("Title")
                .imageUrl("http://example.com/image.jpg")
                .content("Content")
                .tag("Tag")
                .likeCount(50L)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        var postDto = PostDto.builder()
                .id(id)
                .title("Title")
                .imageUrl("http://example.com/image.jpg")
                .content("Content")
                .tag("Tag")
                .likeCount(50L)
                .created(post.getCreated())
                .updated(post.getUpdated())
                .build();

        when(postRepository.findById(id)).thenReturn(post);
        when(toDtoConverter.apply(post)).thenReturn(postDto);

        var result = underTest.findById(id);

        assertNotNull(result);
        assertEquals(postDto, result);
    }

    @Test
    public void testUpdatePost() {
        var postDto = PostDto.builder()
                .id(1L)
                .title("Updated Title")
                .imageUrl("http://example.com/updated.jpg")
                .content("Updated content")
                .tag("Updated Tag")
                .likeCount(200L)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        var post = Post.builder()
                .id(1L)
                .title("Updated Title")
                .imageUrl("http://example.com/updated.jpg")
                .content("Updated content")
                .tag("Updated Tag")
                .likeCount(200L)
                .created(postDto.getCreated())
                .updated(postDto.getUpdated())
                .build();

        when(fromDtoConverter.apply(postDto)).thenReturn(post);

        underTest.updatePost(postDto);

        verify(postRepository, times(1)).update(post);
    }

    @Test
    public void testDeletePost() {
        var id = 1L;

        underTest.deletePost(id);

        verify(postRepository, times(1)).deleteById(id);
    }
}
