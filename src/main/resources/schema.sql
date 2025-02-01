CREATE TABLE IF NOT EXISTS posts
(
	id bigserial PRIMARY KEY,
	name varchar(256) NOT NULL,
	image text NOT NULL,
	description text NOT NULL,
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
	name text NOT NULL
);

CREATE TABLE IF NOT EXISTS posts_tags
(
	tag_id bigserial REFERENCES tags(id) ON DELETE CASCADE,
    post_id bigserial REFERENCES posts(id) ON DELETE CASCADE,
	PRIMARY KEY(post_id, tag_id)
);

------ TEST DATA

INSERT INTO posts(id, name, description, image, like_count)
VALUES (1, 'FIRST POST', 'POST DESCRIPTION', 'img1', 10),
       (2, 'SECOND POST', 'POST DESCRIPTION', 'img2', 20),
       (3, 'THIRD POST', 'POST DESCRIPTION', 'img3', 15);

INSERT INTO comments(post_id, description)
VALUES (1, 'FIRST COMMENT'),
       (1, 'SECOND COMMENT'),
       (2, 'THIRD COMMENT'),
       (1, 'FORTH COMMENT');

INSERT INTO tags(name)
VALUES ('#FIRST_TAG'),
       ('#SECOND_TAG'),
       ('#THIRD_TAG');

INSERT INTO posts_tags(tag_id, post_id)
VALUES (1, 1),
       (1, 2),
       (2, 1),
       (3, 2);