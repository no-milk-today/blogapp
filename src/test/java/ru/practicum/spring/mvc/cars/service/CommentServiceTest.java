package ru.practicum.spring.mvc.cars.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
                .postId(100L)
                .content("Test comment")
                .build();

        var expectedComment = fromDtoConverter.apply(commentDto);
        // according UI behavior
        assertNull(expectedComment.getCreated());
        assertNull(expectedComment.getUpdated());

        var commentCaptor = ArgumentCaptor.forClass(Comment.class);

        var commentFromDB = underTest.addComment(commentDto);

        assertNotNull(commentFromDB.getCreated());
        assertNotNull(commentFromDB.getUpdated());
        assertEquals("Test comment", commentFromDB.getContent());
        assertEquals(100L, commentFromDB.getPostId());

        verify(commentRepository, times(1)).save(commentCaptor.capture());
    }

    @Test
    void testUpdateComment() {
        var commentId = 1L;
        var oldContent = "Old content";
        var newContent = "New updated content";

        var commentFromDb = Comment.builder()
                .id(commentId)
                .content(oldContent)
                .created(LocalDateTime.now().minusDays(1))
                .build();

        var updateDto = CommentDto.builder()
                .id(commentId)
                .content(newContent)
                .build();

        when(commentRepository.findById(commentId)).thenReturn(commentFromDb);

        underTest.updateComment(updateDto);

        verify(commentRepository).findById(commentId);
        verify(commentRepository).update(argThat(comment ->
                comment.getContent().equals(newContent) &&
                        comment.getCreated().equals(commentFromDb.getCreated())
        ));
    }

    @Test
    void testCountCommentsByPostId() {
        when(commentRepository.countByPostId(100L)).thenReturn(5);

        int count = underTest.countCommentsByPostId(100L);

        assertEquals(5, count);
        verify(commentRepository, times(1)).countByPostId(100L);
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