package ru.practicum.spring.mvc.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.spring.mvc.cars.domain.Comment;
import ru.practicum.spring.mvc.cars.dto.CommentDto;
import ru.practicum.spring.mvc.cars.converter.CommentFromDtoConverter;
import ru.practicum.spring.mvc.cars.converter.CommentToDtoConverter;
import ru.practicum.spring.mvc.cars.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    private CommentFromDtoConverter fromDtoConverter;
    private CommentToDtoConverter toDtoConverter;

    private CommentService underTest;

    @BeforeEach
    public void setUp() {
        fromDtoConverter = new CommentFromDtoConverter();
        toDtoConverter = new CommentToDtoConverter();
        underTest = new CommentService(commentRepository, fromDtoConverter, toDtoConverter);
    }

    @Test
    void testAddComment() {
        var commentDto = CommentDto.builder()
                .id(1L)
                .postId(100L)
                .content("Test comment")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        var expectedComment = fromDtoConverter.apply(commentDto);

        underTest.addComment(commentDto);

        verify(commentRepository, times(1)).save(expectedComment);
    }

    @Test
    void testUpdateComment() {
        var commentDto = CommentDto.builder()
                .id(1L)
                .postId(100L)
                .content("Updated comment")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        var expectedComment = fromDtoConverter.apply(commentDto);

        underTest.updateComment(commentDto);

        verify(commentRepository, times(1)).update(expectedComment);
    }

    @Test
    void testFindById() {
        var commentId = 1L;
        var comment = Comment.builder()
                .id(commentId)
                .postId(100L)
                .content("Some comment")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        var expectedDto = toDtoConverter.apply(comment);

        when(commentRepository.findById(commentId)).thenReturn(comment);

        var result = underTest.findById(commentId);

        assertNotNull(result);
        assertEquals(expectedDto, result);
    }

    @Test
    void testFindAllByPostId() {
        var postId = 100L;
        var comment1 = Comment.builder()
                .id(1L)
                .postId(postId)
                .content("Comment 1")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();
        var comment2 = Comment.builder()
                .id(2L)
                .postId(postId)
                .content("Comment 2")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        var commentList = List.of(comment1, comment2);
        when(commentRepository.findAllByPostId(postId)).thenReturn(commentList);

        var result = underTest.findAllByPostId(postId);

        assertEquals(2, result.size());
        assertEquals(toDtoConverter.apply(comment1), result.get(0));
        assertEquals(toDtoConverter.apply(comment2), result.get(1));
    }

    @Test
    void testDeleteComment() {
        var commentId = 1L;

        underTest.deleteComment(commentId);

        verify(commentRepository, times(1)).deleteById(commentId);
    }

    @Test
    void testDeleteCommentsByPostId() {
        var postId = 100L;

        underTest.deleteCommentsByPostId(postId);

        verify(commentRepository, times(1)).deleteByPostId(postId);
    }
}

