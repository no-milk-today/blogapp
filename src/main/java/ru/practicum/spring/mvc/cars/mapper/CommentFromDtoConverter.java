package ru.practicum.spring.mvc.cars.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.spring.mvc.cars.domain.Comment;
import ru.practicum.spring.mvc.cars.dto.CommentDto;

import java.util.function.Function;

@Component
public class CommentFromDtoConverter implements Function<CommentDto, Comment> {

    @Override
    public Comment apply(CommentDto commentDto) {
        if (commentDto == null) {
            return null;
        }
        return Comment.builder()
                .id(commentDto.getId())
                .postId(commentDto.getPostId())
                .content(commentDto.getContent())
                .created(commentDto.getCreated())
                .updated(commentDto.getUpdated())
                .build();
    }
}

