package ru.practicum.spring.mvc.cars.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.spring.mvc.cars.configuration.DataSourceConfiguration;
import ru.practicum.spring.mvc.cars.configuration.WebConfiguration;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, WebConfiguration.class})
@WebAppConfiguration
@TestPropertySource(locations = "classpath:test-application.properties")
public class PostControllerIntegrationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // cleanup tables
        jdbcTemplate.execute("delete from comment");
        jdbcTemplate.execute("delete from post");
        jdbcTemplate.execute("alter table comment alter column id restart with 1");
        jdbcTemplate.execute("alter table post alter column id restart with 1");

        // Post initial data
        jdbcTemplate.update(
                "insert into post (title, image_url, content, tag, like_count, created, updated) " +
                        "values (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                "Новая Tesla Model S", "https://example.com/tesla-model-s.jpg",
                "Обзор новой Tesla Model S с улучшенной батареей.", "Электромобили", 150L
        );
        jdbcTemplate.update(
                "insert into post (title, image_url, content, tag, like_count, created, updated) " +
                        "values (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                "Обзор BMW M3", "https://example.com/bmw_m3.jpg",
                "Детальный обзор BMW M3 2025 года выпуска.", "Спорт", 200L
        );

        // comment initial data
        jdbcTemplate.update(
                "insert into comment (post_id, content, created, updated) values (?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                1L, "Отличный обзор!"
        );
    }

    @Test
    void getPostDetails_shouldReturnHtmlWithPost() throws Exception {
        var postId = 1L;
        var title = "Новая Tesla Model S";

        mockMvc.perform(get("/posts/details").param("postId", String.valueOf(postId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts/post-details"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attributeExists("comments"))
                // Check that the Title in the HTML matches the retrieved title
                .andExpect(xpath("//table/tr[th/text()='Title']/td").string(title));
    }

    @Test
    void listPosts_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/posts/list"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts/list-posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("currentPage"))
                .andExpect(model().attributeExists("totalPages"))
                .andExpect(model().attributeExists("commentCounts"));
    }

    @Test
    void savePost_shouldAddPostAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/save")
                        .param("title", "Новый Пост")
                        .param("imageUrl", "https://example.com/new.jpg")
                        .param("content", "Содержимое нового поста")
                        .param("tag", "Test")
                        .param("likeCount", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/list"));

        // post has been insterted
        var count = jdbcTemplate.queryForObject("select COUNT(*) from post where title = ?", Integer.class, "Новый Пост");
        assertEquals(1, count);
    }

    @Test
    void deletePost_shouldRemovePostAndRedirect() throws Exception {
        mockMvc.perform(get("/posts/delete").param("postId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/list"));

        // post has been deleted
        var count = jdbcTemplate.queryForObject("select COUNT(*) from post where id = ?", Integer.class, 1L);
        assertEquals(0, count);
    }

    @Test
    void likePost_shouldIncrementLikeCountAndReturnJson() throws Exception {
        var postId = 1L;
        int likeCount = jdbcTemplate.queryForObject("select like_count from post where id = ?", Integer.class, postId);

        mockMvc.perform(post("/posts/" + postId + "/like"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.likeCount").value(likeCount + 1));

        // like_count has been increased
        int updatedLikeCount = jdbcTemplate.queryForObject("select like_count from post where id = ?", Integer.class, postId);
        assertEquals(likeCount + 1, updatedLikeCount);
    }

    @Test
    void showFormForAdd_shouldReturnEmptyPostForm() throws Exception {
        mockMvc.perform(get("/posts/showFormForAdd"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/post-form"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", hasProperty("id", nullValue())))
                .andExpect(model().attribute("post", hasProperty("title", nullValue())))
                .andExpect(model().attribute("post", hasProperty("content", nullValue())));
    }

    @Test
    void showFormForUpdate_shouldReturnFilledPostForm() throws Exception {
        var postId = 1L;

        mockMvc.perform(get("/posts/showFormForUpdate").param("postId", String.valueOf(postId)))
                .andExpect(status().isOk())
                .andExpect(view().name("posts/post-form"))
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", hasProperty("id", is(postId))))
                .andExpect(model().attribute("post", hasProperty("title", is("Новая Tesla Model S"))))
                .andExpect(model().attribute("post", hasProperty("content", is("Обзор новой Tesla Model S с улучшенной батареей."))));
    }

    @Test
    void addComment_shouldAddCommentAndReturnJson() throws Exception {
        var postId = 1L;
        String commentContent = "Тестовый комментарий";

        mockMvc.perform(post("/posts/addComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"postId\":" + postId + ",\"content\":\"" + commentContent + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.postId").value(postId))
                .andExpect(jsonPath("$.content").value(commentContent));

        // Проверяем, что комментарий добавлен в базу данных
        var count = jdbcTemplate.queryForObject(
                "select COUNT(*) from comment where post_id = ? and content = ?",
                Integer.class, postId, commentContent);
        assertEquals(1, count);
    }

    @Test
    void deleteComment_shouldRemoveCommentAndRedirect() throws Exception {
        // Добавляем комментарий для удаления
        jdbcTemplate.update(
                "insert into comment (id, post_id, content, created, updated) values (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                99L, 1L, "Комментарий для удаления"
        );

        mockMvc.perform(get("/posts/deleteComment")
                        .param("commentId", "99")
                        .param("postId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/details?postId=1"));

        // Проверяем, что комментарий удалён из базы данных
        var count = jdbcTemplate.queryForObject("select COUNT(*) from comment where id = ?", Integer.class, 99L);
        assertEquals(0, count);
    }

    @Test
    void updateComment_shouldUpdateCommentAndReturnJson() throws Exception {
        // Добавляем комментарий для обновления
        jdbcTemplate.update(
                "insert into comment (id, post_id, content, created, updated) values (?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                100L, 1L, "Старый контент"
        );

        String updatedContent = "Обновлённый контент";

        mockMvc.perform(post("/posts/updateComment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":100,\"content\":\"" + updatedContent + "\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.content").value(updatedContent));

        // Проверяем, что комментарий обновлён в базе данных
        String contentInDb = jdbcTemplate.queryForObject("select content from comment where id = ?", String.class, 100L);
        assertEquals(updatedContent, contentInDb);
    }
}
