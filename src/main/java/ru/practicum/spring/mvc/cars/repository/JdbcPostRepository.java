package ru.practicum.spring.mvc.cars.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.spring.mvc.cars.domain.Post;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
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
        List<Post> posts = jdbcTemplate.query(sql,
                (rs, rowNum) -> {
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
                },
                pageable.getPageSize(), pageable.getOffset()
        );

        // Преобразование списка Post в объект Page
        int total = jdbcTemplate.queryForObject("select count(*) from post", Integer.class);
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

    /**
     * Inserts new post into db.
     * The generated key (ID) for the inserted post is obtained
     * and can be accessed via the provided {@link KeyHolder}.
     *
     * For more details, see:
     * <a href="https://docs.spring.io/spring-framework/docs/3.0.x/reference/jdbc.html#jdbc-auto-genereted-keys">Spring Documentation - Automatically Generated Keys</a>
     *
     * @param post the post to be saved; must not be null
     */
    @Override
    public void save(Post post) {
        final String INSERT_SQL = "insert into post (title, image_url, content, tag, like_count, created, updated) values (?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_SQL, new String[] {"id"});
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getImageUrl());
            ps.setString(3, post.getContent());
            ps.setString(4, post.getTag());
            ps.setLong(5, post.getLikeCount());
            ps.setTimestamp(6, Timestamp.valueOf(post.getCreated()));
            ps.setTimestamp(7, Timestamp.valueOf(post.getUpdated()));
            return ps;
        }, keyHolder);
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
