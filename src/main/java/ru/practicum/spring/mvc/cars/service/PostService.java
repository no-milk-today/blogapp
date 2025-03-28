package ru.practicum.spring.mvc.cars.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.spring.mvc.cars.domain.Post;
import ru.practicum.spring.mvc.cars.dto.PostDto;
import ru.practicum.spring.mvc.cars.converter.PostFromDtoConverter;
import ru.practicum.spring.mvc.cars.converter.PostToDtoConverter;
import ru.practicum.spring.mvc.cars.exception.ResourceNotFoundException;
import ru.practicum.spring.mvc.cars.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostService {

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
        LocalDateTime now = LocalDateTime.now();
        postDto.setCreated(now);
        postDto.setUpdated(now);

        Post post = fromDtoConverter.apply(postDto);
        postRepository.save(post);
        log.info("New saved post: {}", post);
    }

    public void updatePost(PostDto postDto) {
        Post existingPost = postRepository.findById(postDto.getId());

        if (existingPost == null) {
            throw new ResourceNotFoundException("Post not found with id: " + postDto.getId());
        }

        postDto.setCreated(existingPost.getCreated());
        postDto.setUpdated(LocalDateTime.now());

        Post post = fromDtoConverter.apply(postDto);
        postRepository.update(post);
        log.info("Updated post: {}", post);
    }

    public PostDto findById(Long id) {
        Post post = postRepository.findById(id);
        log.debug("Found post by ID {}: {}", id, post);
        return toDtoConverter.apply(post);
    }

    /**
     * Finds all posts with pagination support.
     *
     * @param pageable the pagination information
     * @return a page of post DTOs
     */
    public Page<PostDto> findAll(Pageable pageable) {
        Page<Post> postPage = postRepository.findAll(pageable);
        log.debug("total count in DB:: {}", postPage.getTotalElements());

        List<PostDto> postDtos = postPage.stream()
                .map(toDtoConverter)
                .collect(Collectors.toList());

        return new PageImpl<>(postDtos, pageable, postPage.getTotalElements());
    }

    /**
     * Finds all posts by tag with pagination support.
     *
     * @param tag      tag for filtering
     * @param pageable the pagination information
     * @return a page of post DTOs
     */
    public Page<PostDto> findByTag(String tag, Pageable pageable) {
        Page<Post> postPage = postRepository.findByTag(tag, pageable);
        log.debug("Found {} posts with tag '{}'", postPage.getTotalElements(), tag);

        List<PostDto> postDtos = postPage.stream()
                .map(toDtoConverter)
                .collect(Collectors.toList());

        return new PageImpl<>(postDtos, pageable, postPage.getTotalElements());
    }

    /**
     * Deletes a post by its ID. The repository ensures that related comments are also deleted.
     *
     * @param id ID of the post
     */
    public void deletePost(Long id) {
        postRepository.deleteById(id);
        log.info("Deleted post with ID: {}", id);
    }

    public PostDto incrementLikeCount(Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            log.error("Post with ID {} not found", postId);
            throw new ResourceNotFoundException("Post not found with id: " + postId);
        }

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.update(post);
        log.info("Post with ID {} has been liked.", postId);
        return toDtoConverter.apply(post);
    }

}
