package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;

import java.util.Map;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Comment comment) {
        jdbcTemplate.update("insert into comments(post_id, description) values(:postId, :description)",
                Map.of("postId", comment.getPostId(),
                        "description", comment.getDescription()));
    }

    @Override
    public void update(Comment comment) {
        jdbcTemplate.update("""
                        update comments
                           set description = :description
                        where id = :id and post_id = :postId
                        """,
                Map.of("postId", comment.getPostId(),
                        "description", comment.getDescription(),
                        "id", comment.getId()));
    }

    @Override
    public void deleteById(Long postId, Long commentId) {
        jdbcTemplate.update("delete from comments where id = :commentId and post_id = :postId",
                Map.of("commentId", commentId,
                        "postId", postId));
    }
}
