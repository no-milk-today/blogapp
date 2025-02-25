package ru.practicum.spring.mvc.cars.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDto {
    private Long id;
    private String title;
    private String imageUrl;
    private String content;

    @Setter(AccessLevel.NONE)
    private String description; // вычисляемое поле

    private String tag;
    private Long likeCount;
    private LocalDateTime created;
    private LocalDateTime updated;
}

