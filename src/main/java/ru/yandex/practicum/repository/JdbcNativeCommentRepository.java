package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.Comment;

@Repository
public class JdbcNativeCommentRepository implements CommentRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcNativeCommentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Comment comment) {
        jdbcTemplate.update("insert into comments(post_id, description) values(?, ?)",
                comment.getPostId(), comment.getDescription());
    }

    @Override
    public void update(Comment comment) {
        jdbcTemplate.update("""
                        update comments
                           set description = ?
                        where id = ? and post_id = ?
                        """,
                comment.getDescription(), comment.getId(), comment.getPostId());
    }

    @Override
    public void deleteById(Long postId, Long commentId) {
        jdbcTemplate.update("delete from comments where id = ? and post_id = ?", commentId, postId);
    }
}
