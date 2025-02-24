-- Таблица с пользователями
create table if not exists users(
                                    id bigserial primary key,
                                    first_name varchar(256) not null,
                                    last_name varchar(256) not null,
                                    age integer not null,
                                    active boolean not null
);

insert into users(id, first_name, last_name, age, active) values (1, 'Иван', 'Иванов', 30, true);
insert into users(id, first_name, last_name, age, active) values (2, 'Петр', 'Петров', 25, false);
insert into users(id, first_name, last_name, age, active) values (3, 'Мария', 'Сидорова', 28, true);

-- Создание таблицы "post"
create table if not exists post (
                                    id bigserial primary key,
                                    title VARCHAR(50) NOT NULL,
                                    image_url VARCHAR(255) NOT NULL,
                                    content VARCHAR(255) NOT NULL,
                                    description VARCHAR(100)
                                        GENERATED ALWAYS AS (SUBSTRING(content, 1, 100)), -- Автоматическое вычисление
                                    tag VARCHAR(50),
                                    like_count BIGINT NOT NULL,
                                    created TIMESTAMP WITH TIME ZONE NOT NULL,
                                    updated TIMESTAMP WITH TIME ZONE NOT NULL
);

-- Вставка тестовых данных в таблицу "post"
insert into post
(id, title, image_url, content, tag, like_count, created, updated)
values
    (
        1,
        'Новая Tesla Model S',
        'https://example.com/tesla_model_s.jpg',
        'Обзор новой Tesla Model S с улучшенной батареей.',
        'Электромобили',
        150,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        2,
        'Обзор BMW M3 2025',
        'https://example.com/bmw_m3_2025.jpg',
        'Детальный обзор BMW M3 2025 года выпуска.',
        'Спортивные автомобили',
        200,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        3,
        'Сравнение Audi A4 и Mercedes C-Class',
        'https://example.com/audi_a4_vs_mercedes_c_class.jpg',
        'Сравнительный анализ Audi A4 и Mercedes C-Class.',
        'Седаны',
        180,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP);

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
-- Вставка тестовых данных в таблицу "comment"
insert into comment (post_id, content, created, updated)
values (1, 'Отличный обзор! Хотелось бы узнать больше про дальность хода.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'Tesla снова удивляет! Отличная работа.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'BMW M3 – просто зверь! Интересно, как он в сравнении с C63 AMG.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'Хороший обзор, но хотелось бы больше деталей про подвеску.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'Audi A4 мне больше нравится по дизайну, но Mercedes комфортнее.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'Спасибо за сравнение! Очень полезная информация.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
