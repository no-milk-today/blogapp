package ru.practicum.spring.mvc.test.dto;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        Integer age,
        boolean active) {
}
