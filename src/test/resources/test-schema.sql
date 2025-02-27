-- Удаление таблицы "comment"
DROP TABLE IF EXISTS comment;

-- Удаление таблицы "post"
DROP TABLE IF EXISTS post;

-- Создание таблицы "post"
CREATE TABLE post (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    title VARCHAR(50) NOT NULL,
                                    image_url VARCHAR(255) NOT NULL,
                                    content VARCHAR(255) NOT NULL,
                                    description VARCHAR(100)
                                        GENERATED ALWAYS AS (SUBSTRING(content, 1, 50)), -- Автоматическое вычисление
                                    tag VARCHAR(50),
                                    like_count BIGINT NOT NULL,
                                    created TIMESTAMP WITH TIME ZONE NOT NULL,
                                    updated TIMESTAMP WITH TIME ZONE NOT NULL
);
-- Создание таблицы "comment"
CREATE TABLE comment (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         post_id BIGINT NOT NULL,
                         content VARCHAR(255) NOT NULL,
                         created TIMESTAMP WITH TIME ZONE NOT NULL,
                         updated TIMESTAMP WITH TIME ZONE NOT NULL,
                         FOREIGN KEY (post_id) REFERENCES post(id) -- ON DELETE CASCADE
);