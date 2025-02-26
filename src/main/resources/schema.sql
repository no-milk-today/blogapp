-- Таблица с пользователями
create table if not exists users(
                                    id bigserial primary key,
                                    first_name varchar(256) not null,
                                    last_name varchar(256) not null,
                                    age integer not null,
                                    active boolean not null
);

insert into users(first_name, last_name, age, active) values ('Иван', 'Иванов', 30, true);
insert into users(first_name, last_name, age, active) values ('Петр', 'Петров', 25, false);
insert into users(first_name, last_name, age, active) values ('Мария', 'Сидорова', 28, true);

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
(title, image_url, content, tag, like_count, created, updated)
values
    (
        'Новая Tesla Model S',
        'https://hips.hearstapps.com/hmg-prod/images/2025-tesla-model-s-1-672d42e172407.jpg?crop=0.465xw:0.466xh;0.285xw,0.361xh&resize=2048:*',
        'Обзор новой Tesla Model S с улучшенной батареей.',
        'Электромобили',
        150,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Обзор BMW M3 2025',
        'https://th.bing.com/th/id/OIP.bzYFg35ON46k9aOrygsDiQHaEK?rs=1&pid=ImgDetMain',
        'Детальный обзор BMW M3 2025 года выпуска.',
        'Спортивные автомобили',
        200,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Сравнение Audi A4 и Mercedes C-Class',
        'https://cdn-ds.com/media/websites/3060/content/2018-MB-C-Class-vs-2018-Audi-A4_A_o.jpg?s=247784',
        'Сравнительный анализ Audi A4 и Mercedes C-Class.',
        'Седаны',
        180,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Ford Mustang Mach-E: электрический мустанг',
        'https://electrek.co/wp-content/uploads/sites/3/2023/04/Ford-Mustang-Mach-E-GT-1.jpg',
        'Полный обзор электрического кроссовера Ford Mustang Mach-E с тест-драйвом.',
        'Электромобили',
        95,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Porsche 911 GT3 RS 2024',
        'https://cdn.motor1.com/images/mgl/6ZZr2/s1/porsche-911-gt3-rs-2023.jpg',
        'Эксклюзивный тест-драйв нового Porsche 911 GT3 RS на гоночном треке.',
        'Спортивные автомобили',
        320,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Toyota Hydrogen Engine: будущее топлива',
        'https://www.toyota-europe.com/news/images/2021/hydrogen-engine-corolla-1.jpg',
        'Инновационная водородная силовая установка Toyota в реальных тестах.',
        'Технологии',
        278,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Лучшие внедорожники 2024 года',
        'https://cdn.jdpower.com/ArticleImages/2024-SUV-Lineup.jpg',
        'Рейтинг топ-5 внедорожников по версии Automotive Awards 2024.',
        'Внедорожники',
        154,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Volvo EX90: безопасность превыше всего',
        'https://www.volvocars.com/images/ex90-gallery-1.jpg',
        'Первое знакомство с электрическим флагманом Volvo с системой LiDAR.',
        'Электромобили',
        216,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Ретро-реставрация Chevrolet Camaro 1969',
        'https://www.classiccarrestoration.com/images/camaro69-process.jpg',
        'Полная реставрация легендарного muscle car с модернизацией под капотом.',
        'Ретро автомобили',
        189,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Hyundai Ioniq 6: дизайн из будущего',
        'https://www.hyundai.com/content/hyundai/ww/data/news/data/2023/ioniq6-gallery-1.jpg',
        'Подробный разговор с главным дизайнером Hyundai о концепции Streamliner.',
        'Дизайн',
        132,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Гоночные технологии в серийных авто',
        'https://www.motorsportmagazine.com/images/tech-transfer-f1.jpg',
        'Как инновации из Formula 1 попадают в обычные автомобили.',
        'Технологии',
        245,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Lamborghini Revuelto: гибридный суперкар',
        'https://cdn.lamborghini.com/images/revuelto/main-gallery-3.jpg',
        'Первые впечатления от гибридной силовой установки нового флагмана Lamborghini.',
        'Суперкары',
        415,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Зарядные станции: инфраструктура будущего',
        'https://electrek.co/wp-content/uploads/sites/3/2023/01/EV-charging-station-network.jpg',
        'Анализ развития зарядной инфраструктуры для электромобилей в Европе.',
        'Инфраструктура',
        87,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Mercedes EQS SUV: роскошь на электричестве',
        'https://www.mbusa.com/content/dam/mbusa/vehicles/2023/EQS-SUV/Gallery/01.jpg',
        'Тестирование флагманского электрического SUV от Mercedes-Benz.',
        'Электромобили',
        302,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Глубокий тюнинг Nissan GTR R35',
        'https://www.topgear.com/sites/default/files/tuning-gtr-r35.jpg',
        'Экстремальная модификация двигателя RB26 для трековых заездов.',
        'Тюнинг',
        178,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Автопилот Tesla: текущие возможности',
        'https://www.tesla.com/sites/default/files/autopilot-navigation.jpg',
        'Подробный разбор функционала FSD Beta 12.3 на реальных дорогах.',
        'Автономное вождение',
        429,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Раллийный спорт: технологии WRC 2024',
        'https://dirtfish.com/wp-content/uploads/2023/11/WRC-2024-car-tech.jpg',
        'Как устроены раллийные автомобили нового поколения чемпионата мира.',
        'Ралли',
        193,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Классика против современности: Jaguar E-Type',
        'https://www.classicdriver.com/sites/default/files/jaguar-e-type-comparo.jpg',
        'Сравнение оригинального E-Type 1961 года с современной реинкарнацией.',
        'Ретро автомобили',
        264,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Зимние тесты новых шин Michelin',
        'https://www.tire-reviews.com/images/winter-test-2024.jpg',
        'Сравнение зимних шин ведущих брендов в условиях Крайнего Севера.',
        'Шины',
        121,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Китайские электромобили: угроза Европе?',
        'https://carnewschina.com/images/china-ev-invasion.jpg',
        'Анализ экспансии BYD, NIO и Xpeng на европейский рынок.',
        'Рынок',
        156,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Rolls-Royce Spectre: электрическая роскошь',
        'https://www.rolls-roycemotorcars.com/content/dam/rrmc/spectre/gallery-1.jpg',
        'Первые детали о первом электрическом Rolls-Royce в истории.',
        'Люкс',
        387,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Дрифт-культура в Японии',
        'https://www.driftjapan.com/images/daikoku-parking-night.jpg',
        'Экскурсия в мир подпольных дрифт-мероприятий Токио и Осаки.',
        'Культура',
        498,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Aston Martin Valkyrie: дорожный гиперкар',
        'https://www.astonmartin.com/content/valkyrie-gallery-1.jpg',
        'Эксклюзивный тест гиперкара с технологиями Formula 1.',
        'Гиперкары',
        572,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
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
-- Вставка тестовых данных в таблицу "comment"
insert into comment (post_id, content, created, updated)
values (1, 'Отличный обзор! Хотелось бы узнать больше про дальность хода.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (1, 'Tesla снова удивляет! Отличная работа.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'BMW M3 – просто зверь! Интересно, как он в сравнении с C63 AMG.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 'Хороший обзор, но хотелось бы больше деталей про подвеску.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'Audi A4 мне больше нравится по дизайну, но Mercedes комфортнее.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 'Спасибо за сравнение! Очень полезная информация.', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
