package ru.practicum.spring.mvc.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.spring.mvc.cars.domain.Comment;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JdbcCommentRepositoryTest extends AbstractDaoTest {

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        super.setUp();
        // Добавление тестовых комментариев
        jdbcTemplate.execute("INSERT INTO comment (id, post_id, content, created, updated) VALUES (1, 1, 'Первый комментарий', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO comment (id, post_id, content, created, updated) VALUES (2, 1, 'Второй комментарий', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO comment (id, post_id, content, created, updated) VALUES (3, 2, 'Третий комментарий', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
    }

    @Test
    void findAllByPostId() {
        var comments = commentRepository.findAllByPostId(1L);

        assertNotNull(comments);
        assertEquals(2, comments.size());

        var comment = comments.get(0);
        assertEquals(1L, comment.getId());
        assertEquals(1L, comment.getPostId());
        assertEquals("Первый комментарий", comment.getContent());
    }

    @Test
    void findById() {
        var comment = commentRepository.findById(1L);

        assertNotNull(comment);
        assertEquals(1L, comment.getId());
        assertEquals(1L, comment.getPostId());
        assertEquals("Первый комментарий", comment.getContent());
    }

    @Test
    void save() {
        var newComment = Comment.builder()
                .id(4L)
                .postId(1L)
                .content("Новый комментарий")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        commentRepository.save(newComment);

        var savedComment = commentRepository.findById(4L);
        assertNotNull(savedComment);
        assertEquals("Новый комментарий", savedComment.getContent());
    }

    @Test
    void update() {
        var commentToUpdate = commentRepository.findById(1L);
        assertNotNull(commentToUpdate);

        commentToUpdate.setContent("Обновленный комментарий");
        commentRepository.update(commentToUpdate);

        var updatedComment = commentRepository.findById(1L);
        assertEquals("Обновленный комментарий", updatedComment.getContent());
    }

    @Test
    void deleteById() {
        commentRepository.deleteById(1L);

        assertThrows(Exception.class, () -> commentRepository.findById(1L));
    }

    @Test
    void deleteByPostId() {
        commentRepository.deleteByPostId(1L);

        var comments = commentRepository.findAllByPostId(1L);
        assertTrue(comments.isEmpty());
    }
}