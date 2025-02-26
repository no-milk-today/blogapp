package ru.practicum.spring.mvc.cars.converter;

import org.springframework.stereotype.Component;
import ru.practicum.spring.mvc.cars.domain.Post;
import ru.practicum.spring.mvc.cars.dto.PostDto;

import java.util.function.Function;

@Component
public class PostFromDtoConverter implements Function<PostDto, Post> {

    @Override
    public Post apply(PostDto postDto) {
        if (postDto == null) {
            return null;
        }
        return Post.builder()
                .id(postDto.getId())
                .title(postDto.getTitle())
                .imageUrl(postDto.getImageUrl())
                .content(postDto.getContent())
                .tag(postDto.getTag())
                .likeCount(postDto.getLikeCount())
                .created(postDto.getCreated())
                .updated(postDto.getUpdated())
                .build();
    }
}
