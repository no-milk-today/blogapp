package ru.practicum.spring.mvc.cars.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.spring.mvc.cars.domain.Post;
import ru.practicum.spring.mvc.cars.dto.PostDto;
import ru.practicum.spring.mvc.cars.mapper.PostFromDtoConverter;
import ru.practicum.spring.mvc.cars.mapper.PostToDtoConverter;
import ru.practicum.spring.mvc.cars.repository.PostRepository;

import java.time.LocalDateTime;
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
        LocalDateTime now = LocalDateTime.now();
        postDto.setCreated(now);
        postDto.setUpdated(now);

        Post post = fromDtoConverter.apply(postDto);
        postRepository.save(post);
        LOG.info("New saved post: {}", post);
    }

    public void updatePost(PostDto postDto) {
        Post existingPost = postRepository.findById(postDto.getId());

        if (existingPost == null) {
            throw new IllegalArgumentException("Post not found with id: " + postDto.getId());
        }

        postDto.setCreated(existingPost.getCreated());
        postDto.setUpdated(LocalDateTime.now());

        Post post = fromDtoConverter.apply(postDto);
        postRepository.update(post);
        LOG.info("Updated post: {}", post);
    }

    public PostDto findById(Long id) {
        Post post = postRepository.findById(id);
        LOG.debug("Found post by ID {}: {}", id, post);
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
        LOG.debug("total count in DB:: {}", postPage.getTotalElements());

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
        LOG.debug("Found {} posts with tag '{}'", postPage.getTotalElements(), tag);

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
        LOG.info("Deleted post with ID: {}", id);
    }

    public PostDto incrementLikeCount(Long postId) {
        Post post = postRepository.findById(postId);
        if (post == null) {
            LOG.error("Post with ID {} not found", postId);
            throw new IllegalArgumentException("Post not found with id: " + postId);
        }

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.update(post);
        LOG.info("Post with ID {} has been liked.", postId);
        return toDtoConverter.apply(post);
    }

}
