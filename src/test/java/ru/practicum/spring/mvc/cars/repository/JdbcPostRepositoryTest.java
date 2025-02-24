package ru.practicum.spring.mvc.cars.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.spring.mvc.cars.domain.Post;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JdbcPostRepositoryTest extends AbstractDaoTest {

    @Autowired
    private PostRepository postRepository;

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
        var post = postRepository.findById(1L);

        assertNotNull(post);
        assertEquals(1L, post.getId());
        assertEquals("Новая Tesla Model S", post.getTitle());
    }

    @Test
    void save() {
        var newPost = Post.builder()
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

        var savedPost = postRepository.findById(4L);
        assertNotNull(savedPost);
        assertEquals("Обзор Ford Mustang 2025", savedPost.getTitle());
        assertNotNull(savedPost.getDescription()); // auto-generated in DB
    }

    @Test
    void update() {
        var postToUpdate = postRepository.findById(1L);
        assertNotNull(postToUpdate);

        postToUpdate.setTitle("Обновленный обзор Tesla Model S");
        postRepository.update(postToUpdate);

        var updatedPost = postRepository.findById(1L);
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
