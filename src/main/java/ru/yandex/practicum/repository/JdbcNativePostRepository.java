package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Objects;

@Repository
public class JdbcNativePostRepository implements PostRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativePostRepository(JdbcTemplate jdbcTemplate) {
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
                                where p.id = ?
                                order by t.id, c.id
                                """,
                        new PostMapper(),
                        id
                ))
                .getFirst();
    }

    @Override
    public void save(Post post) {
        jdbcTemplate.update("insert into posts(title, content, image) values(?, ?, ?)",
                post.getTitle(), post.getContent(), post.getImage());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("delete from posts where id = ?", id);
    }
}
