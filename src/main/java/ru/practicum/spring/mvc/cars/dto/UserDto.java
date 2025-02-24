package ru.practicum.spring.mvc.cars.dto;

public record UserDto(
        Long id,
        String firstName,
        String lastName,
        Integer age,
        boolean active) {
}
