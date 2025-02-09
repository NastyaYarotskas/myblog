package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Post> findAll() {
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
                               c.description as comment_description
                        from posts p 
                                 left join posts_tags pt on p.id = pt.post_id
                                 left join tags t on pt.tag_id = t.id
                                 left join comments c on p.id = c.post_id
                        order by p.id
                        """,
                new PostMapper()
        );
    }

    @Override
    public Post findById(Long id) {
        return Objects.requireNonNull(jdbcTemplate.query(
                        """
                                select p.id,
                                       p.title, 
                                       p.image, 
                                       p.content, 
                                       p.like_count,
                                       t.id as tag_id,
                                       t.name as tag_name,
                                       c.id as comment_id,
                                       c.description as comment_description
                                from posts p 
                                         left join posts_tags pt on p.id = pt.post_id
                                         left join tags t on pt.tag_id = t.id
                                         left join comments c on p.id = c.post_id
                                where p.id = :id
                                order by t.id, c.id
                                """,
                        Map.of("id", id),
                        new PostMapper()
                ))
                .getFirst();
    }

    @Override
    public List<Post> filterByTags(Set<Long> tags) {
        return jdbcTemplate.query(
                """
                        with filtered_posts as (
                            select p.*
                            from posts p 
                                     left join posts_tags pt on p.id = pt.post_id
                                     left join tags t on pt.tag_id = t.id
                            where t.id in (:tags)
                        )
                        select p.id,
                               p.title, 
                               p.image, 
                               p.content, 
                               p.like_count,
                               t.id as tag_id,
                               t.name as tag_name,
                               c.id as comment_id,
                               c.description as comment_description
                        from filtered_posts p 
                                 left join posts_tags pt on p.id = pt.post_id
                                 left join tags t on pt.tag_id = t.id
                                 left join comments c on p.id = c.post_id
                        order by p.id
                        """,
                Map.of("tags", tags),
                new PostMapper()
        );
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update("insert into posts(title, content, image) values(:title, :content, :image)",
                Map.of("title", post.getTitle(),
                        "content", post.getContent(),
                        "image", post.getImage()));
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
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from posts where id = :id", Map.of("id", id));
    }
}
