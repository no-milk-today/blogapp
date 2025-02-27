package ru.practicum.spring.mvc.cars.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {
    private Long id;
    private Long postId;
    private String content;
    @JsonIgnore
    private LocalDateTime created;
    @JsonIgnore
    private LocalDateTime updated;
}
