package ru.practicum.spring.mvc.cars.repository;

import ru.practicum.spring.mvc.cars.domain.Comment;

import java.util.List;

public interface CommentRepository {
    List<Comment> findAllByPostId(Long postId);
    Comment findById(Long id);
    void save(Comment post);
    void update(Comment post);
    void deleteById(Long id);
    void deleteByPostId(Long postId);
    int countByPostId(Long postId);
}
