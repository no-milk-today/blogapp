package ru.practicum.spring.mvc.cars.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.practicum.spring.mvc.cars.domain.Comment;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
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
        final String INSERT_SQL = "INSERT INTO comment (post_id, content, created, updated) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"id"});
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
