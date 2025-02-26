package ru.yandex.practicum.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.error.PostNotFoundException;
import ru.yandex.practicum.mapper.PostMapper;
import ru.yandex.practicum.mapper.PostsMapper;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.util.Map;
import java.util.NoSuchElementException;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Page<Post> findAll(int page, int size) {

        return jdbcTemplate.query(
                """
                        with paged_posts as (
                            select id, (select count(*) from posts) as total
                            from posts
                            order by id desc
                            limit :limit
                            offset :offset
                        )
                        select p.id,
                               p.title, 
                               p.image, 
                               p.content, 
                               p.like_count,
                               pp.total,
                               t.id as tag_id,
                               t.name as tag_name,
                               c.id as comment_id,
                               c.description as comment_description
                        from paged_posts pp
                                 inner join posts p on p.id = pp.id 
                                 left join posts_tags pt on p.id = pt.post_id
                                 left join tags t on pt.tag_id = t.id
                                 left join comments c on p.id = c.post_id
                        order by p.id desc
                        """,
                Map.of("limit", size, "offset", page * size),
                new PostsMapper()
        );
    }

    @Override
    public Post findById(Long id) {
        try {
            return jdbcTemplate.query(
                    """
                            select p.id,
                                   p.title, 
                                   p.image, 
                                   p.content, 
                                   p.like_count,
                                   t.id as tag_id,
                                   t.name as tag_name,
                                   c.id as comment_id,
                                   c.description as comment_description,
                                   1 as total
                            from posts p 
                                     left join posts_tags pt on p.id = pt.post_id
                                     left join tags t on pt.tag_id = t.id
                                     left join comments c on p.id = c.post_id
                            where p.id = :id
                            order by t.id, c.id
                            """,
                    Map.of("id", id),
                    new PostMapper()
            ).getFirst();
        } catch (EmptyResultDataAccessException | NoSuchElementException e) {
            throw new PostNotFoundException(id);
        }
    }

    @Override
    public Page<Post> filterByTags(int page, int size, Long tagId) {
        return jdbcTemplate.query(
                """
                        with filtered_posts as (
                            select p.id, count(*) as total
                            from posts p 
                                     inner join posts_tags pt on p.id = pt.post_id
                                     inner join tags t on pt.tag_id = t.id
                            where t.id = :tagId
                            group by p.id
                        )
                        select p.id,
                               p.title, 
                               p.image, 
                               p.content, 
                               p.like_count,
                               pp.total,
                               t.id as tag_id,
                               t.name as tag_name,
                               c.id as comment_id,
                               c.description as comment_description
                        from filtered_posts pp
                                 inner join posts p on pp.id = p.id
                                 left join posts_tags pt on p.id = pt.post_id
                                 left join tags t on pt.tag_id = t.id
                                 left join comments c on p.id = c.post_id
                        order by p.id
                        """,
                Map.of("tagId", tagId),
                new PostsMapper()
        );
    }

    @Override
    public void save(Post post) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource().addValues(
                Map.of("title", post.getTitle(),
                        "content", post.getContent(),
                        "image", post.getImage()
                )
        );
        jdbcTemplate.update("insert into posts(title, content, image) values(:title, :content, :image)",
                params,
                keyHolder
        );

        long postId = keyHolder.getKey().longValue();

        for (Tag tag : post.getTags()) {
            Long tagId;
            try {
                tagId = jdbcTemplate.queryForObject(
                        "SELECT id FROM tags WHERE name = :name",
                        Map.of("name", tag.getName()),
                        (rs, num) -> rs.getLong("id")
                );
            } catch (EmptyResultDataAccessException exception) {
                tagId = null;
            }

            if (tagId == null) {
                KeyHolder tagKeyHolder = new GeneratedKeyHolder();
                MapSqlParameterSource tagParams = new MapSqlParameterSource().addValues(
                        Map.of("tagName", tag.getName())
                );
                jdbcTemplate.update("INSERT INTO tags(name) VALUES (:tagName)",
                        tagParams, tagKeyHolder);
                tagId = tagKeyHolder.getKey().longValue();
            }

            jdbcTemplate.update(
                    "INSERT INTO posts_tags(post_id, tag_id) VALUES (:postId, :tagId)",
                    Map.of("tagId", tagId, "postId", postId)
            );
        }
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update("""
                        update posts
                           set title = :title,
                               content = :content,
                               image = :image
                        where id = :id
                        """,
                Map.of("title", post.getTitle(),
                        "content", post.getContent(),
                        "image", post.getImage(),
                        "id", post.getId())
        );

        jdbcTemplate.update(
                "DELETE FROM posts_tags WHERE post_id = :postId",
                Map.of("postId", post.getId())
        );

        for (Tag tag : post.getTags()) {
            Long tagId;
            try {
                tagId = jdbcTemplate.queryForObject(
                        "SELECT id FROM tags WHERE name = :name",
                        Map.of("name", tag.getName()),
                        (rs, num) -> rs.getLong("id")
                );
            } catch (EmptyResultDataAccessException exception) {
                tagId = null;
            }

            if (tagId == null) {
                KeyHolder tagKeyHolder = new GeneratedKeyHolder();
                MapSqlParameterSource tagParams = new MapSqlParameterSource().addValues(
                        Map.of("tagName", tag.getName())
                );
                jdbcTemplate.update("INSERT INTO tags(name) VALUES (:tagName)",
                        tagParams, tagKeyHolder);
                tagId = tagKeyHolder.getKey().longValue();
            }

            jdbcTemplate.update(
                    "INSERT INTO posts_tags(post_id, tag_id) VALUES (:postId, :tagId)",
                    Map.of("tagId", tagId, "postId", post.getId())
            );
        }
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from posts where id = :id", Map.of("id", id));
    }

    @Override
    public void likePost(Long id) {
        jdbcTemplate.update("""
                        update posts
                           set like_count = like_count + 1
                        where id = :id
                        """,
                Map.of("id", id)
        );
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from posts", Map.of());
        jdbcTemplate.update("ALTER TABLE posts ALTER COLUMN id RESTART WITH 1", Map.of());
    }
}
