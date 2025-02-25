package ru.practicum.spring.mvc.cars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.spring.mvc.cars.domain.Post;

import java.util.List;

public interface PostRepository {
    Page<Post> findAll(Pageable pageable);
    Post findById(Long id);
    void save(Post post);
    void update(Post post);
    void deleteById(Long id);
    void deleteAll();
}
