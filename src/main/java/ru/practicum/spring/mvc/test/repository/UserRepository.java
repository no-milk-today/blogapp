package ru.practicum.spring.mvc.test.repository;

import ru.practicum.spring.mvc.test.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    List<User> findAll();
    void deleteById(Long id);
}
