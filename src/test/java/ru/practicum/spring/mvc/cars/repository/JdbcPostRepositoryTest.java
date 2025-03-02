package ru.practicum.spring.mvc.cars.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.spring.mvc.cars.domain.Post;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JdbcPostRepositoryTest extends AbstractDaoTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 3); // Первая страница, размер страницы - 3
        Page<Post> postsPage = postRepository.findAll(pageable);

        assertNotNull(postsPage);
        assertEquals(3, postsPage.getSize()); // Проверка размера страницы

        var posts = postsPage.getContent();
        assertFalse(posts.isEmpty());

        var post = posts.get(0);
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
    void findByTag() {
        var pageable = PageRequest.of(0, 3);
        var tag = "Электромобили";
        var postsPage = postRepository.findByTag(tag, pageable);

        assertNotNull(postsPage);
        assertTrue(postsPage.getTotalElements() > 0);

        var posts = postsPage.getContent();
        assertFalse(posts.isEmpty());

        for (Post post : posts) {
            assertEquals(tag, post.getTag());
        }
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
