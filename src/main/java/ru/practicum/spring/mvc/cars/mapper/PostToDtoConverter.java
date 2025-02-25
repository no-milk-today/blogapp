package ru.practicum.spring.mvc.cars.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.spring.mvc.cars.domain.Post;
import ru.practicum.spring.mvc.cars.dto.PostDto;

import java.util.function.Function;

@Component
public class PostToDtoConverter implements Function<Post, PostDto> {

    @Override
    public PostDto apply(Post post) {
        if (post == null) {
            return null;
        }
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .imageUrl(post.getImageUrl())
                .content(post.getContent())
                .description(post.getDescription()) // это поле генерируется БД
                .tag(post.getTag())
                .likeCount(post.getLikeCount())
                .created(post.getCreated())
                .updated(post.getUpdated())
                .build();
    }
}

