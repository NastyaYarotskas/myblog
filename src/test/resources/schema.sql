DROP TABLE IF EXISTS posts_tags;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS comments;
DROP TABLE IF EXISTS posts;

CREATE TABLE IF NOT EXISTS posts
(
	id bigserial PRIMARY KEY,
	title varchar(256) NOT NULL,
	image text NOT NULL,
	content text NOT NULL,
	like_count int DEFAULT 0
);

CREATE TABLE IF NOT EXISTS comments
(
	id bigserial PRIMARY KEY,
	post_id bigserial REFERENCES posts(id) ON DELETE CASCADE,
	description text NOT NULL
);

CREATE TABLE IF NOT EXISTS tags
(
	id bigserial PRIMARY KEY,
	name text NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS posts_tags
(
	tag_id bigserial REFERENCES tags(id) ON DELETE CASCADE,
    post_id bigserial REFERENCES posts(id) ON DELETE CASCADE,
	PRIMARY KEY(post_id, tag_id)
);

------ TEST DATA

INSERT INTO posts(title, content, image, like_count)
VALUES ('Рецепт идеального чизкейка', 'Чизкейк — это один из моих любимых десертов.', 'img1', 20),
       ('Топ-5 книг для саморазвития', 'Книги — это мощный инструмент для саморазвития.', 'img2', 10),
       ('Как начать бегать: советы для новичков', 'Бег — это отличный способ поддерживать себя в форме.', 'img3', 15),
       ('Как выбрать ноутбук для работы и учёбы', 'Выбор ноутбука — это важное решение.', 'img4', 12),
       ('Путешествие в горы: впечатления и советы', 'Недавно я вернулся из поездки в горы.', 'img5', 10);

INSERT INTO comments(post_id, description)
VALUES (1, 'Спасибо за рецепт! Попробовала сделать, получилось очень вкусно. Теперь это мой любимый десерт.'),
       (1, 'А можно ли заменить творожный сыр на что-то другое? У меня на него аллергия.'),
       (2, 'Спасибо за подборку! Уже прочитала две книги из списка, и они действительно вдохновляют.'),
       (2, 'А есть ли у вас рекомендации по книгам о финансовой грамотности?'),
       (3, 'Спасибо за советы! Начал бегать две недели назад, и ваши рекомендации очень помогли.'),
       (3, 'А как часто нужно бегать, чтобы увидеть результат? И как избежать боли в мышцах?'),
       (4, 'Спасибо за статью! Как раз выбираю ноутбук для учёбы, и ваши советы очень помогли.'),
       (4, 'А какой ноутбук вы бы посоветовали для работы с графикой? Хочу купить что-то не слишком дорогое, но качественное.'),
       (5, 'Отличная статья! Спасибо за советы по экипировке. Как раз планирую поездку в горы в следующем месяце.'),
       (5, 'А вы не могли бы подробнее рассказать о маршрутах? Хотелось бы узнать, какие из них подходят для новичков.');

INSERT INTO tags(name)
VALUES ('Рецепты'),
       ('Десерты'),
       ('Чизкейк'),
       ('Книги'),
       ('Саморазвитие'),
       ('Мотивация'),
       ('Спорт'),
       ('Бег'),
       ('Здоровье'),
       ('Технологии'),
       ('Ноутбуки'),
       ('Советы'),
       ('Путешествия'),
       ('Горы');

INSERT INTO posts_tags(post_id, tag_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (2, 4),
       (2, 5),
       (2, 6),
       (3, 7),
       (3, 8),
       (3, 9),
       (4, 10),
       (4, 11),
       (4, 12),
       (5, 13),
       (5, 14),
       (5, 12);