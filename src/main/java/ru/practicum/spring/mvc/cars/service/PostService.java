package ru.practicum.spring.mvc.cars.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.spring.mvc.cars.domain.Post;
import ru.practicum.spring.mvc.cars.dto.PostDto;
import ru.practicum.spring.mvc.cars.mapper.PostFromDtoConverter;
import ru.practicum.spring.mvc.cars.mapper.PostToDtoConverter;
import ru.practicum.spring.mvc.cars.repository.PostRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Logger LOG = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;
    private final PostFromDtoConverter fromDtoConverter;
    private final PostToDtoConverter toDtoConverter;

    @Autowired
    public PostService(PostRepository postRepository, PostFromDtoConverter fromDtoConverter, PostToDtoConverter toDtoConverter) {
        this.postRepository = postRepository;
        this.fromDtoConverter = fromDtoConverter;
        this.toDtoConverter = toDtoConverter;
    }

    public void addPost(PostDto postDto) {
        Post post = fromDtoConverter.apply(postDto);
        postRepository.save(post);
        LOG.info("New saved post: {}", post);
    }

    public void updatePost(PostDto postDto) {
        Post post = fromDtoConverter.apply(postDto);
        postRepository.update(post);
        LOG.info("Updated post: {}", post);
    }

    public PostDto findById(Long id) {
        Post post = postRepository.findById(id);
        LOG.debug("Found post by ID {}: {}", id, post);
        return toDtoConverter.apply(post);
    }

    public List<PostDto> findAll() {
        List<Post> posts = postRepository.findAll();
        LOG.debug("total count in DB:: {}", posts.size());
        return posts.stream()
                .map(toDtoConverter)
                .collect(Collectors.toList());
    }

    /**
     * Deletes a post by its ID. The repository ensures that related comments are also deleted.
     *
     * @param id ID of the post
     */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
        LOG.info("Deleted post with ID: {}", id);
        System.out.println();
    }
}
