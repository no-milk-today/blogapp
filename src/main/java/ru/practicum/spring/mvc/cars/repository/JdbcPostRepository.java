package ru.practicum.spring.mvc.cars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.spring.mvc.cars.domain.Post;

import java.util.List;

@Repository
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;
    private final JdbcCommentRepository commentRepository;

    public JdbcPostRepository(JdbcTemplate jdbcTemplate, JdbcCommentRepository commentRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.commentRepository = commentRepository;
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        String sql = "select * from post limit ? offset ?";
        List<Post> posts = jdbcTemplate.query(sql, new Object[]{pageable.getPageSize(), pageable.getOffset()}, (rs, rowNum) -> {
            Post post = Post.builder()
                    .id(rs.getLong("id"))
                    .title(rs.getString("title"))
                    .imageUrl(rs.getString("image_url"))
                    .content(rs.getString("content"))
                    .description(rs.getString("description"))
                    .tag(rs.getString("tag"))
                    .likeCount(rs.getLong("like_count"))
                    .created(rs.getTimestamp("created").toLocalDateTime())
                    .updated(rs.getTimestamp("updated").toLocalDateTime())
                    .build();
            return post;
        });

        String countSql = "select count(*) from post";
        int total = jdbcTemplate.queryForObject(countSql, Integer.class);

        return new PageImpl<>(posts, pageable, total);
    }

    @Override
    public Post findById(Long id) {
        String sql = "select * from post where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Post.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .imageUrl(rs.getString("image_url"))
                .content(rs.getString("content"))
                .description(rs.getString("description"))
                .tag(rs.getString("tag"))
                .likeCount(rs.getLong("like_count"))
                .created(rs.getTimestamp("created").toLocalDateTime())
                .updated(rs.getTimestamp("updated").toLocalDateTime())
                .build(), id);
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update(
                "insert into post (id, title, image_url, content, tag, like_count, created, updated) values (?, ?, ?, ?, ?, ?, ?, ?)",
                post.getId(),
                post.getTitle(),
                post.getImageUrl(),
                post.getContent(),
                post.getTag(),
                post.getLikeCount(),
                post.getCreated(),
                post.getUpdated()
        );
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update(
                "update post set title = ?, image_url = ?, content = ?, tag = ?, like_count = ?, created = ?, updated = ? where id = ?",
                post.getTitle(),
                post.getImageUrl(),
                post.getContent(),
                post.getTag(),
                post.getLikeCount(),
                post.getCreated(),
                post.getUpdated(),
                post.getId()
        );
    }

    /**
     * Deletes a post by its ID. This method performs two operations:
     * 1. Deletes all comments associated with the given post.
     * 2. Deletes the post itself from the database.
     *
     * @param postId the ID of the post to be deleted
     */
    @Transactional
    public void deleteById(Long postId) {
        commentRepository.deleteByPostId(postId);
        String sql = "DELETE FROM post WHERE id = ?";
        jdbcTemplate.update(sql, postId);
    }

    @Override
    public void deleteAll() {
        String sql = "delete from post";
        jdbcTemplate.update(sql);
    }
}
