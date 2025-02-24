package ru.practicum.spring.mvc.cars.repository;

import ru.practicum.spring.mvc.cars.domain.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    List<User> findAll();
    void deleteById(Long id);
}
