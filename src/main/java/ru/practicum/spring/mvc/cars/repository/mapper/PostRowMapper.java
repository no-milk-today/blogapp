package ru.practicum.spring.mvc.cars.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.practicum.spring.mvc.cars.domain.Post;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PostRowMapper implements RowMapper<Post> {

    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Post.builder()
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
    }
}
