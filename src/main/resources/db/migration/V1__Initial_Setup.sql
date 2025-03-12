-- Создание таблицы "post"
create table if not exists post (
                                    id bigserial primary key,
                                    title VARCHAR(50) NOT NULL,
                                    image_url VARCHAR(255) NOT NULL,
                                    content VARCHAR(255) NOT NULL,
                                    description VARCHAR(100)
                                        GENERATED ALWAYS AS (SUBSTRING(content, 1, 50)) STORED,
                                    tag VARCHAR(50),
                                    like_count BIGINT NOT NULL,
                                    created TIMESTAMP WITH TIME ZONE NOT NULL,
                                    updated TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Создание таблицы "comment"
create table if not exists comment
(
    id      bigserial PRIMARY KEY,
    post_id BIGINT                   NOT NULL,
    content VARCHAR(255)             NOT NULL,
    created TIMESTAMP WITH TIME ZONE NOT NULL,
    updated TIMESTAMP WITH TIME ZONE NOT NULL,
    FOREIGN KEY (post_id) REFERENCES post (id) -- ON DELETE CASCADE
);