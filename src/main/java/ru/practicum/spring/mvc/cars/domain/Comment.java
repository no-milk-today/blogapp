package ru.practicum.spring.mvc.cars.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Comment {
    private Long id;
    private Long postId;
    private String content;
    private LocalDateTime created;
    private LocalDateTime updated;
}
