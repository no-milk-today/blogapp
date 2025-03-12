package ru.practicum.spring.mvc.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractDaoTest extends AbstractTestcontainers {

    protected JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        // clean data
        jdbcTemplate = getJdbcTemplate();
        jdbcTemplate.execute("TRUNCATE TABLE comment RESTART IDENTITY CASCADE");
        jdbcTemplate.execute("TRUNCATE TABLE post RESTART IDENTITY CASCADE");

        // fill data
        jdbcTemplate.execute("INSERT INTO post (title, image_url, content, tag, like_count, created, updated) VALUES ('Новая Tesla Model S', 'https://example.com/tesla_model_s.jpg', 'Обзор новой Tesla Model S с улучшенной батареей.', 'Электромобили', 150, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO post (title, image_url, content, tag, like_count, created, updated) VALUES ('Обзор BMW M3 2025', 'https://example.com/bmw_m3_2025.jpg', 'Детальный обзор BMW M3 2025 года выпуска.', 'Спортивные автомобили', 200, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
        jdbcTemplate.execute("INSERT INTO post (title, image_url, content, tag, like_count, created, updated) VALUES ('Сравнение Audi A4 и Mercedes C-Class', 'https://example.com/audi_a4_vs_mercedes_c_class.jpg', 'Сравнительный анализ Audi A4 и Mercedes C-Class.', 'Седаны', 180, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)");
    }
}