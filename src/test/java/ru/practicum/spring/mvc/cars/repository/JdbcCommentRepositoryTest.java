package ru.practicum.spring.mvc.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.spring.mvc.cars.domain.Comment;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS) // to refresh the context (and connection pool) avoiding stale pool issues
class JdbcCommentRepositoryTest extends AbstractDaoTest {

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        super.setUp();
        // Clear existing data from the comment table and reset the sequence
        jdbcTemplate.execute("TRUNCATE TABLE comment RESTART IDENTITY CASCADE");

        // Добавление тестовых комментариев
        jdbcTemplate.execute("INSERT INTO comment (post_id, content, created, updated) VALUES (1, 'Первый комментарий', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO comment (post_id, content, created, updated) VALUES (1, 'Второй комментарий', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO comment (post_id, content, created, updated) VALUES (2, 'Третий комментарий', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
    }

    @Test
    void findAllByPostId() {
        var comments = commentRepository.findAllByPostId(1L);

        assertNotNull(comments);
        assertEquals(2, comments.size());

        var comment = comments.getFirst();
        assertEquals(1L, comment.getId());
        assertEquals(1L, comment.getPostId());
        assertEquals("Первый комментарий", comment.getContent());
    }

    @Test
    void findById() {
        var existingID = commentRepository.findAllByPostId(1L).getFirst().getId();
        var comment = commentRepository.findById(existingID);

        assertNotNull(comment);
        assertEquals(existingID, comment.getId());
        assertEquals(1L, comment.getPostId());
        assertEquals("Первый комментарий", comment.getContent());
    }

    @Test
    void save() {
        var newComment = Comment.builder()
                .postId(1L)
                .content("Новый комментарий")
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        assertEquals(2, commentRepository.findAllByPostId(1L).size());
        commentRepository.save(newComment);
        assertEquals(3, commentRepository.findAllByPostId(1L).size());
    }

    @Test
    void update() {
        var existingID = commentRepository.findAllByPostId(1L).getFirst().getId();
        var commentToUpdate = commentRepository.findById(existingID);
        assertNotNull(commentToUpdate);

        commentToUpdate.setContent("Обновленный комментарий");
        commentRepository.update(commentToUpdate);

        var updatedComment = commentRepository.findById(existingID);
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

    @Test
    void countByPostId() {
        int count = commentRepository.countByPostId(1L);
        assertEquals(2, count);

        count = commentRepository.countByPostId(2L);
        assertEquals(1, count);

        count = commentRepository.countByPostId(3L);
        assertEquals(0, count);
    }
}