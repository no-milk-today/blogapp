package ru.practicum.spring.mvc.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.spring.mvc.cars.configuration.DataSourceConfiguration;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcPostRepository.class, JdbcCommentRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public abstract class AbstractDaoTest {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // Очистка базы данных
        jdbcTemplate.execute("DELETE FROM comment");
        jdbcTemplate.execute("DELETE FROM post");

        // Добавление тестовых данных
        jdbcTemplate.execute("INSERT INTO post (id, title, image_url, content, tag, like_count, created, updated) VALUES (1, 'Новая Tesla Model S', 'https://example.com/tesla_model_s.jpg', 'Обзор новой Tesla Model S с улучшенной батареей.', 'Электромобили', 150, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO post (id, title, image_url, content, tag, like_count, created, updated) VALUES (2, 'Обзор BMW M3 2025', 'https://example.com/bmw_m3_2025.jpg', 'Детальный обзор BMW M3 2025 года выпуска.', 'Спортивные автомобили', 200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO post (id, title, image_url, content, tag, like_count, created, updated) VALUES (3, 'Сравнение Audi A4 и Mercedes C-Class', 'https://example.com/audi_a4_vs_mercedes_c_class.jpg', 'Сравнительный анализ Audi A4 и Mercedes C-Class.', 'Седаны', 180, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
    }

}
