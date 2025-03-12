# Блог о машинах

Простое веб-приложение на **Spring Boot**, представляющее блог о машинах.

## Технологии

- **Java 21**, **Spring Boot 3.4.3**
- **Maven** – сборка проекта
- **Thymeleaf** – серверный шаблонизатор HTML с поддержкой JavaScript
- **JdbcTemplate** – работа с базой данных
- **PostgreSQL** – основная база данных
- **Flyway** – миграции базы данных
- **JUnit 5** – модульные и интеграционные тесты
- **Testcontainers** – тестирование с использованием контейнеров

## Сборка и запуск

1. **Поднять docker compose:**

   ```bash
   export PG_USERNAME=yourDesiredUsername
   export PG_PASSWORD=yourDesiredPassword
   docker-compose up -d
   ```

2. **Запуск приложения:**

Прокинуть env переменные `PG_USERNAME=значение` и `PG_PASSWORD=значение`.

```bash
$env:PG_USERNAME="значение"; $env:PG_PASSWORD="значение"; 
java -jar .\target\cars-0.0.1-SNAPSHOT.jar
```

## Доступ к приложению

- **Лента постов:**

  ```
  http://localhost:8080/posts/list
  ```

- **Страница поста:**

  ```
  http://localhost:8080/posts/details?postId=1
  ```

## Тестирование

- **Запуск тестов:**

  ```bash
  mvn test
  ```

  Тесты включают модульные и интеграционные, охватывающие основные компоненты приложения.

## Дополнительная информация

- **Схема базы данных и начальные данные** находятся в файле `src/main/resources/db/migration/V1__Initial_Setup.sql`.
- Приложение использует PostgreSQL.