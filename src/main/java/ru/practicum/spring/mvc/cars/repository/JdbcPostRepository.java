package ru.practicum.spring.mvc.cars.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.practicum.spring.mvc.cars.domain.Post;

import java.util.List;

@Repository
public class JdbcPostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcPostRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
        return jdbcTemplate.query("select * from post", (rs, rowNum) -> {
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

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from post where id = ?", id);
    }

    @Override
    public void deleteAll() {
        String sql = "delete from post";
        jdbcTemplate.update(sql);
    }
}
