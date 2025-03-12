package ru.practicum.spring.mvc.cars.repository;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;

@Testcontainers
public abstract class AbstractTestcontainers {

    @ServiceConnection
    @Container
    protected static final PostgreSQLContainer<?> postgreSQLContainer =
            new PostgreSQLContainer<>("postgres:latest");

    @BeforeAll
    static void beforeAll() {
        var flyway = Flyway.configure().dataSource(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword()
        ).load();
        flyway.migrate();
    }

//    @BeforeAll
//    static void beforeAll() {
//        DataSource dataSource = DataSourceBuilder.create()
//                .driverClassName(postgreSQLContainer.getDriverClassName())
//                .url(postgreSQLContainer.getJdbcUrl())
//                .username(postgreSQLContainer.getUsername())
//                .password(postgreSQLContainer.getPassword())
//                .build();
//
//        Flyway flyway = Flyway.configure()
//                .dataSource(dataSource)
//                .load();
//        flyway.migrate();
//    }


    private static DataSource getDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgreSQLContainer.getDriverClassName())
                .url(postgreSQLContainer.getJdbcUrl())
                .username(postgreSQLContainer.getUsername())
                .password(postgreSQLContainer.getPassword())
                .build();
    }

    protected static JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }
}