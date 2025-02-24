package ru.practicum.spring.mvc.cars.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.spring.mvc.cars.domain.Comment;

import java.util.List;

@Repository
public class JdbcCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        String sql = "select * from comment where post_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> Comment.builder()
                .id(rs.getLong("id"))
                .postId(rs.getLong("post_id"))
                .content(rs.getString("content"))
                .created(rs.getTimestamp("created").toLocalDateTime())
                .updated(rs.getTimestamp("updated").toLocalDateTime())
                .build(), postId);
    }

    @Override
    public Comment findById(Long id) {
        String sql = "select * from comment where id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> Comment.builder()
                .id(rs.getLong("id"))
                .postId(rs.getLong("post_id"))
                .content(rs.getString("content"))
                .created(rs.getTimestamp("created").toLocalDateTime())
                .updated(rs.getTimestamp("updated").toLocalDateTime())
                .build(), id);
    }

    @Override
    public void save(Comment comment) {
        String sql = "insert into comment (id, post_id, content, created, updated) values (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, comment.getId(), comment.getPostId(), comment.getContent(), comment.getCreated(), comment.getUpdated());
    }

    @Override
    public void update(Comment comment) {
        String sql = "update comment set post_id = ?, content = ?, created = ?, updated = ? where id = ?";
        jdbcTemplate.update(sql, comment.getPostId(), comment.getContent(), comment.getCreated(), comment.getUpdated(), comment.getId());
    }

    @Override
    public void deleteById(Long id) {
        String sql = "delete from comment where id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void deleteByPostId(Long postId) {
        String sql = "delete from comment where post_id = ?";
        jdbcTemplate.update(sql, postId);
    }
}
