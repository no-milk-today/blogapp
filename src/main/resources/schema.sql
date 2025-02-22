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
        CURRENT_TIMESTAMP
    );