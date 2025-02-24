package ru.practicum.spring.mvc.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.spring.mvc.cars.configuration.DataSourceConfiguration;
import ru.practicum.spring.mvc.cars.domain.Post;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcPostRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
class JdbcPostRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void setUp() {
        // Очистка базы данных
        jdbcTemplate.execute("DELETE FROM post");

        // Добавление тестовых данных
        jdbcTemplate.execute("INSERT INTO post (id, title, image_url, content, tag, like_count, created, updated) VALUES (1, 'Новая Tesla Model S', 'https://example.com/tesla_model_s.jpg', 'Обзор новой Tesla Model S с улучшенной батареей.', 'Электромобили', 150, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO post (id, title, image_url, content, tag, like_count, created, updated) VALUES (2, 'Обзор BMW M3 2025', 'https://example.com/bmw_m3_2025.jpg', 'Детальный обзор BMW M3 2025 года выпуска.', 'Спортивные автомобили', 200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO post (id, title, image_url, content, tag, like_count, created, updated) VALUES (3, 'Сравнение Audi A4 и Mercedes C-Class', 'https://example.com/audi_a4_vs_mercedes_c_class.jpg', 'Сравнительный анализ Audi A4 и Mercedes C-Class.', 'Седаны', 180, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");

    }

    @Test
    void findAll() {
        var posts = postRepository.findAll();

        assertNotNull(posts);
        assertEquals(3, posts.size());

        var post = posts.getFirst();
        assertEquals(1L, post.getId());
        assertEquals("Новая Tesla Model S", post.getTitle());
    }

    @Test
    void findById() {
        Post post = postRepository.findById(1L);

        assertNotNull(post);
        assertEquals(1L, post.getId());
        assertEquals("Новая Tesla Model S", post.getTitle());
    }

    @Test
    void save() {
        Post newPost = Post.builder()
                .id(4L)
                .title("Обзор Ford Mustang 2025")
                .imageUrl("https://example.com/ford_mustang_2025.jpg")
                .content("Обзор Ford Mustang 2025 года выпуска.")
                .tag("Спортивные автомобили")
                .likeCount(120L)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .build();

        postRepository.save(newPost);

        Post savedPost = postRepository.findById(4L);
        assertNotNull(savedPost);
        assertEquals("Обзор Ford Mustang 2025", savedPost.getTitle());
        assertNotNull(savedPost.getDescription()); // auto-generated in DB
    }

    @Test
    void update() {
        Post postToUpdate = postRepository.findById(1L);
        assertNotNull(postToUpdate);

        postToUpdate.setTitle("Обновленный обзор Tesla Model S");
        postRepository.update(postToUpdate);

        Post updatedPost = postRepository.findById(1L);
        assertEquals("Обновленный обзор Tesla Model S", updatedPost.getTitle());
    }

    @Test
    void deleteById() {
        postRepository.deleteById(1L);

        assertThrows(Exception.class, () -> {
            postRepository.findById(1L);
        });
    }
}
