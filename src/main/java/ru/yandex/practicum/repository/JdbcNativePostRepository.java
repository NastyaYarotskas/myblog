package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;

import java.util.Map;
import java.util.Objects;

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
                ))
                .getCollection().getFirst();
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
