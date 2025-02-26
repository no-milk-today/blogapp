package ru.practicum.spring.mvc.cars.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.spring.mvc.cars.configuration.DataSourceConfiguration;
import ru.practicum.spring.mvc.cars.configuration.WebConfiguration;

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

        // clean table and fill the data
        jdbcTemplate.execute("DELETE FROM post");
        jdbcTemplate.update(
                "INSERT INTO post (title, image_url, content, tag, like_count, created, updated) " +
                        "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                "Новая Tesla Model S",
                "https://example.com/tesla-model-s.jpg",
                "Обзор новой Tesla Model S с улучшенной батареей.",
                "Электромобили",
                150
        );
        jdbcTemplate.update(
                "INSERT INTO post (title, image_url, content, tag, like_count, created, updated) " +
                        "VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)",
                "Обзор BMW M3",
                "https://example.com/bmw_m3.jpg",
                "Детальный обзор BMW M3 2025 года выпуска.",
                "Спорт",
                200
        );
    }

    @Test
    void getPostDetails_shouldReturnHtmlWithPost() throws Exception {
        // Query the database to get the ID and Title of the first post
        var post = jdbcTemplate.queryForMap("SELECT id, title FROM post ORDER BY id LIMIT 1");
        var postId = ((Number) post.get("id")).longValue();
        var title = (String) post.get("title");

        mockMvc.perform(get("/posts/details").param("postId", String.valueOf(postId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts/post-details"))
                .andExpect(model().attributeExists("post"))
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
                .andExpect(model().attributeExists("totalPages"));
    }

    @Test
    void savePost_shouldAddPostAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/save")
                        .param("title", "Новый Post")
                        .param("imageUrl", "https://example.com/new.jpg")
                        .param("content", "Содержимое нового поста")
                        .param("tag", "Test")
                        .param("likeCount", "0"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/list"));
    }

    @Test
    void deletePost_shouldRemovePostAndRedirect() throws Exception {
        mockMvc.perform(get("/posts/delete").param("postId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/list"));
    }

    @Test
    void likePost_shouldIncrementLikeCountAndReturnJson() throws Exception {
        // get the ID and like count of the first founded post from DB
        var post = jdbcTemplate.queryForMap("SELECT id, like_count FROM post ORDER BY id LIMIT 1");
        var postId = ((Number) post.get("id")).longValue();
        int likeCount = ((Number) post.get("like_count")).intValue();

        mockMvc.perform(post("/posts/" + postId + "/like"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.likeCount").value(likeCount + 1));
    }


}
