package ru.practicum.spring.mvc.cars.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.practicum.spring.mvc.cars.configuration.DataSourceConfiguration;
import ru.practicum.spring.mvc.cars.domain.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcNativeUserRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
class JdbcNativeUserRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Очистка базы данных
        jdbcTemplate.execute("DELETE FROM users");

        // Добавление тестовых данных
        jdbcTemplate.execute("INSERT INTO users (id, first_name, last_name, age, active) VALUES (1, 'Иван', 'Иванов', 30, true)");
        jdbcTemplate.execute("INSERT INTO users (id, first_name, last_name, age, active) VALUES (2, 'Петр', 'Петров', 25, false)");
        jdbcTemplate.execute("INSERT INTO users (id, first_name, last_name, age, active) VALUES (3, 'Мария', 'Сидорова', 28, true)");
    }

    @Test
    void save_shouldAddUserToDatabase() {
        User user = new User(4L, "Петр", "Васильев", 25, true);

        userRepository.save(user);

        User savedUser = userRepository.findAll().stream()
                .filter(createdUsers -> createdUsers.getId().equals(4L))
                .findFirst()
                .orElse(null);

        assertNotNull(savedUser);
        assertEquals("Петр", savedUser.getFirstName());
        assertEquals("Васильев", savedUser.getLastName());
    }

    @Test
    void findAll_shouldReturnAllUsers() {
        List<User> users = userRepository.findAll();

        assertNotNull(users);
        assertEquals(3, users.size());

        User user = users.getFirst();
        assertEquals(1L, user.getId());
        assertEquals("Иван", user.getFirstName());
    }

    @Test
    void deleteById_shouldRemoveUserFromDatabase() {
        userRepository.deleteById(1L);

        List<User> users = userRepository.findAll();

        User deletedUser = users.stream()
                .filter(createdUsers -> createdUsers.getId().equals(1L))
                .findFirst()
                .orElse(null);
        assertNull(deletedUser);
    }
}
