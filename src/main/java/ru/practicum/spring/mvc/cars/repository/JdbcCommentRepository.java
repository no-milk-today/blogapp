package ru.practicum.spring.mvc.cars.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.spring.mvc.cars.domain.Comment;
import ru.practicum.spring.mvc.cars.repository.mapper.CommentRowMapper;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class JdbcCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;
    private final CommentRowMapper commentRowMapper;

    public JdbcCommentRepository(JdbcTemplate jdbcTemplate, CommentRowMapper commentRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.commentRowMapper = commentRowMapper;
    }

    @Override
    public List<Comment> findAllByPostId(Long postId) {
        String sql = "select * from comment where post_id = ?";
        return jdbcTemplate.query(sql, commentRowMapper, postId);
    }

    @Override
    public Comment findById(Long id) {
        String sql = "select * from comment where id = ?";
        return jdbcTemplate.queryForObject(sql, commentRowMapper, id);
    }

    @Override
    public void save(Comment comment) {
        final String INSERT_SQL = "INSERT INTO comment (post_id, content, created, updated) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[]{"id"});
            ps.setLong(1, comment.getPostId());
            ps.setString(2, comment.getContent());
            ps.setTimestamp(3, Timestamp.valueOf(comment.getCreated()));
            ps.setTimestamp(4, Timestamp.valueOf(comment.getUpdated()));
            return ps;
        }, keyHolder);
        comment.setId(keyHolder.getKey().longValue());
    }

    @Override
    public void update(Comment comment) {
        String sql = "update comment set  content = ?, updated = ? where id = ?";
        jdbcTemplate.update(sql, comment.getContent(), comment.getUpdated(), comment.getId());
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

    @Override
    public int countByPostId(Long postId) {
        String sql = "select count(*) from comment where post_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, postId);
    }
}
