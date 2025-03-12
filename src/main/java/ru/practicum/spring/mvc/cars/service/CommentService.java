package ru.practicum.spring.mvc.cars.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.spring.mvc.cars.domain.Comment;
import ru.practicum.spring.mvc.cars.dto.CommentDto;
import ru.practicum.spring.mvc.cars.converter.CommentFromDtoConverter;
import ru.practicum.spring.mvc.cars.converter.CommentToDtoConverter;
import ru.practicum.spring.mvc.cars.repository.CommentRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentFromDtoConverter fromDtoConverter;
    private final CommentToDtoConverter toDtoConverter;

    @Autowired
    public CommentService(CommentRepository commentRepository,
                          CommentFromDtoConverter fromDtoConverter,
                          CommentToDtoConverter toDtoConverter) {
        this.commentRepository = commentRepository;
        this.fromDtoConverter = fromDtoConverter;
        this.toDtoConverter = toDtoConverter;
    }

    public CommentDto addComment(CommentDto commentDto) {
        Comment comment = fromDtoConverter.apply(commentDto);
        LocalDateTime currentTimestamp = LocalDateTime.now();
        comment.setCreated(currentTimestamp);
        comment.setUpdated(currentTimestamp);

        log.debug("Comment before saving in DB: {}", comment);
        commentRepository.save(comment);
        log.debug("Comment after saving in DB: {}", comment);

        // Обновляем CommentDto актуальными данными
        commentDto.setId(comment.getId());
        commentDto.setCreated(comment.getCreated());
        commentDto.setUpdated(comment.getUpdated());
        log.info("Added new comment with ID {}, with postId: {}", comment.getId(), comment.getPostId());
        return commentDto;
    }

    public void updateComment(CommentDto commentDto) {
        Comment existingComment = commentRepository.findById(commentDto.getId());

        if (existingComment == null) {
            throw new IllegalArgumentException("Comment not found with id: " + commentDto.getId());
        }

        // commentDto.setPostId(existingComment.getPostId()); // с клиента приходил JSON без постId
        commentDto.setCreated(existingComment.getCreated());
        commentDto.setUpdated(LocalDateTime.now());
        log.debug("DTO to check: {}", commentDto);
        Comment comment = fromDtoConverter.apply(commentDto);
        commentRepository.update(comment);
        log.info("Updated comment: {}", comment);
    }

    public CommentDto findById(Long id) {
        Comment comment = commentRepository.findById(id);
        log.debug("Found comment by ID {}: {}", id, comment);
        return toDtoConverter.apply(comment);
    }

    public List<CommentDto> findAllByPostId(Long postId) {
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        log.debug("Found {} comments for post with ID {}", comments.size(), postId);
        return comments.stream()
                .map(toDtoConverter)
                .collect(Collectors.toList());
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
        log.info("Deleted comment with ID: {}", id);
    }

    /**
     * Removes comments associated with given postId
     *
     * @param postId ID of the post.
     */
    public void deleteCommentsByPostId(Long postId) {
        commentRepository.deleteByPostId(postId);
        log.info("Deleted all comments for post with ID: {}", postId);
    }

    public int countCommentsByPostId(Long postId) {
        int countByPostId = commentRepository.countByPostId(postId);
        log.debug("Comments count: {} by postId: {}", countByPostId, postId);
        return countByPostId;
    }
}
