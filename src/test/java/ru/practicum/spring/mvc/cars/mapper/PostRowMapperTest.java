package ru.practicum.spring.mvc.cars.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.spring.mvc.cars.domain.Post;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PostRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        // Given
        var postRowMapper = new PostRowMapper();
        var resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("title")).thenReturn("Test Title");
        when(resultSet.getString("image_url")).thenReturn("http://example.com/image.jpg");
        when(resultSet.getString("content")).thenReturn("Test Content");
        when(resultSet.getString("description")).thenReturn("Test Content");
        when(resultSet.getString("tag")).thenReturn("Test Tag");
        when(resultSet.getLong("like_count")).thenReturn(100L);
        when(resultSet.getTimestamp("created")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        when(resultSet.getTimestamp("updated")).thenReturn(Timestamp.valueOf(LocalDateTime.now()));
        // When
        var actual = postRowMapper.mapRow(resultSet, 1);
        // Then
        var expected = Post.builder()
                .id(resultSet.getLong("id"))
                .title(resultSet.getString("title"))
                .imageUrl(resultSet.getString("image_url"))
                .content(resultSet.getString("content"))
                .description(resultSet.getString("description"))
                .tag(resultSet.getString("tag"))
                .likeCount(resultSet.getLong("like_count"))
                .created(resultSet.getTimestamp("created").toLocalDateTime())
                .updated(resultSet.getTimestamp("updated").toLocalDateTime())
                .build();
        assertThat(actual).isEqualTo(expected);
    }
}