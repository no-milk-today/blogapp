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

-- Вставка тестовых данных в таблицу "post"
insert into post
(title, image_url, content, tag, like_count, created, updated)
values
    (
        'Новая Tesla Model S',
        'https://hips.hearstapps.com/hmg-prod/images/2025-tesla-model-s-1-672d42e172407.jpg?crop=0.465xw:0.466xh;0.285xw,0.361xh&resize=2048:*',
        'Полный обзор обновленной Tesla Model S: батарея 100 кВт·ч с запасом хода 650 км, революционная система охлаждения и новый интерьер с горизонтальным дисплеем.',
        'Электромобили',
        150,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Обзор BMW M3 2025',
        'https://th.bing.com/th/id/OIP.bzYFg35ON46k9aOrygsDiQHaEK?rs=1&pid=ImgDetMain',
        'Тест-драйв нового BMW M3 Competition: 3.0-литровый рядный шестицилиндровый двигатель мощностью 510 л.с., система полного привода xDrive и адаптивная подвеска.',
        'Спортивные автомобили',
        200,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Сравнение Audi A4 и Mercedes C-Class',
        'https://cdn-ds.com/media/websites/3060/content/2018-MB-C-Class-vs-2018-Audi-A4_A_o.jpg?s=247784',
        'Детальное сравнение Audi A4 45 TFSI и Mercedes C300: анализ динамики, эргономики салона, мультимедийных систем и стоимости обслуживания.',
        'Седаны',
        180,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Ford Mustang Mach-E: электрический мустанг',
        'https://electrek.co/wp-content/uploads/sites/3/2023/04/Ford-Mustang-Mach-E-GT-1.jpg',
        'Экспресс-тест Ford Mustang Mach-E GT: разгон до 100 км/ч за 3.7 сек, интеллектуальная система полного привода и особенности фирменного дизайна кузова.',
        'Электромобили',
        95,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Porsche 911 GT3 RS 2024',
        'https://cdn.motor1.com/images/mgl/6ZZr2/s1/porsche-911-gt3-rs-2023.jpg',
        'Испытания Porsche 911 GT3 RS на трассе Нюрбургринг: 4.0-литровый атмосферный двигатель 525 л.с., аэродинамический комплект с активным антикрылом.',
        'Спортивные автомобили',
        320,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Toyota Hydrogen Engine: будущее топлива',
        'https://www.toyota-europe.com/news/images/2021/hydrogen-engine-corolla-1.jpg',
        'Экспериментальный водородный двигатель Toyota: принцип работы, сравнение с электромобилями и перспективы внедрения в серийное производство.',
        'Технологии',
        278,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Лучшие внедорожники 2024 года',
        'https://cdn.jdpower.com/ArticleImages/2024-SUV-Lineup.jpg',
        'Топ-5 внедорожников года: сравнение проходимости, комфорта салона и технологий безопасности в моделях Land Rover Defender, Toyota Land Cruiser и Mercedes G-Class.',
        'Внедорожники',
        154,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Volvo EX90: безопасность превыше всего',
        'https://www.volvocars.com/images/ex90-gallery-1.jpg',
        'Первые впечатления от Volvo EX90: система LiDAR с дальностью обнаружения 250 метров, экологичные материалы салона и автономное вождение уровня 3.',
        'Электромобили',
        216,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Ретро-реставрация Chevrolet Camaro 1969',
        'https://www.classiccarrestoration.com/images/camaro69-process.jpg',
        'Полная реставрация Camaro SS 1969: замена кузовных панелей, установка современного 6.2-литрового двигателя LT1 и адаптация классического дизайна.',
        'Ретро автомобили',
        189,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Hyundai Ioniq 6: дизайн из будущего',
        'https://www.hyundai.com/content/hyundai/ww/data/news/data/2023/ioniq6-gallery-1.jpg',
        'Интервью с главным дизайнером Hyundai: философия аэродинамического дизайна Ioniq 6, коэффициент лобового сопротивления 0.21 и эргономика салона.',
        'Дизайн',
        132,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Гоночные технологии в серийных авто',
        'https://www.motorsportmagazine.com/images/tech-transfer-f1.jpg',
        'Адаптация технологий Formula 1: керамические тормоза, активные аэродинамические элементы и системы рекуперации энергии в гражданских автомобилях.',
        'Технологии',
        245,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Lamborghini Revuelto: гибридный суперкар',
        'https://cdn.lamborghini.com/images/revuelto/main-gallery-3.jpg',
        'Технический обзор Lamborghini Revuelto: гибридная установка 1015 л.с., углеродное шасси и система векторного распределения крутящего момента.',
        'Суперкары',
        415,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Зарядные станции: инфраструктура будущего',
        'https://electrek.co/wp-content/uploads/sites/3/2023/01/EV-charging-station-network.jpg',
        'Развитие сети зарядных станций в Европе: стандарты CCS и NACS, скорость зарядки до 350 кВт и интеграция с renewable energy sources.',
        'Инфраструктура',
        87,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Mercedes EQS SUV: роскошь на электричестве',
        'https://www.mbusa.com/content/dam/mbusa/vehicles/2023/EQS-SUV/Gallery/01.jpg',
        'Тест-драйв Mercedes EQS 580 4MATIC: запас хода 600 км, система MBUX Hyperscreen и адаптивная пневмоподвеска AIRMATIC.',
        'Электромобили',
        302,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Глубокий тюнинг Nissan GTR R35',
        'https://www.topgear.com/sites/default/files/tuning-gtr-r35.jpg',
        'Модификация Nissan GT-R до 800 л.с.: установка турбин Garrett GTX, интеркулера увеличенного объема и кастомной системы управления ECU.',
        'Тюнинг',
        178,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Автопилот Tesla: текущие возможности',
        'https://www.tesla.com/sites/default/files/autopilot-navigation.jpg',
        'Анализ FSD Beta 12.3: навигация в городских условиях, распознавание дорожных знаков и взаимодействие со сложными перекрестками.',
        'Автономное вождение',
        429,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Раллийный спорт: технологии WRC 2024',
        'https://dirtfish.com/wp-content/uploads/2023/11/WRC-2024-car-tech.jpg',
        'Технические особенности раллийных автомобилей: гибридные силовые установки, антилаг система и материалы кузова из углепластика.',
        'Ралли',
        193,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Классика против современности: Jaguar E-Type',
        'https://www.classicdriver.com/sites/default/files/jaguar-e-type-comparo.jpg',
        'Сравнение Jaguar E-Type 1961 года с современной репликой: оригинальный 3.8-литровый двигатель против электрической силовой установки 400 л.с.',
        'Ретро автомобили',
        264,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Зимние тесты новых шин Michelin',
        'https://www.tire-reviews.com/images/winter-test-2024.jpg',
        'Сравнение зимних шин Michelin X-ICE Snow и Bridgestone Blizzak: торможение на льду, управляемость на снегу и износостойкость протектора.',
        'Шины',
        121,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Китайские электромобили: угроза Европе?',
        'https://carnewschina.com/images/china-ev-invasion.jpg',
        'Анализ моделей BYD Seal, NIO ET5 и Xpeng G9: ценовая политика, технологические решения и стратегии выхода на европейский рынок.',
        'Рынок',
        156,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Rolls-Royce Spectre: электрическая роскошь',
        'https://www.rolls-roycemotorcars.com/content/dam/rrmc/spectre/gallery-1.jpg',
        'Первый электрический Rolls-Royce: запас хода 520 км, шумоизоляция кабины 100 дБ и кастомная система адаптивной подвески Magic Carpet Ride.',
        'Люкс',
        387,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Дрифт-культура в Японии',
        'https://www.driftjapan.com/images/daikoku-parking-night.jpg',
        'Эксклюзивный репортаж с дрифт-мероприятий в Токио: модифицированные Nissan Silvia S15, Toyota Supra и Mazda RX-7 в ночных гонках.',
        'Культура',
        498,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ),
    (
        'Aston Martin Valkyrie: дорожный гиперкар',
        'https://www.astonmartin.com/content/valkyrie-gallery-1.jpg',
        'Испытания Aston Martin Valkyrie: гибридная силовая установка 1160 л.с., активная аэродинамика и кресла из углеродного волокна с 6-точечными ремнями.',
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
