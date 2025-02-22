package ru.practicum.spring.mvc.test.domain;


import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Post {
    private Long id;
    private String title;
    private String imageUrl;
    private String content;

    @Setter(AccessLevel.NONE)
    private String description; // generated field

    private String tag;
    private Long likeCount;
    private LocalDateTime created;
    private LocalDateTime updated;
}
