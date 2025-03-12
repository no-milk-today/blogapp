package ru.practicum.spring.mvc.cars.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.spring.mvc.cars.domain.Comment;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CommentRowMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Comment.builder()
                .id(rs.getLong("id"))
                .postId(rs.getLong("post_id"))
                .content(rs.getString("content"))
                .created(rs.getTimestamp("created").toLocalDateTime())
                .updated(rs.getTimestamp("updated").toLocalDateTime())
                .build();
    }
}