package ru.practicum.spring.mvc.cars.repository;

import ru.practicum.spring.mvc.cars.domain.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();
    Post findById(Long id);
    void save(Post post);
    void update(Post post);
    void deleteById(Long id);
}
