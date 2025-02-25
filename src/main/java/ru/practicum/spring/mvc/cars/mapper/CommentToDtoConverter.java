package ru.practicum.spring.mvc.cars.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.spring.mvc.cars.domain.Comment;
import ru.practicum.spring.mvc.cars.dto.CommentDto;

import java.util.function.Function;

@Component
public class CommentToDtoConverter implements Function<Comment, CommentDto> {

    @Override
    public CommentDto apply(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentDto.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .content(comment.getContent())
                .created(comment.getCreated())
                .updated(comment.getUpdated())
                .build();
    }
}

