package ru.yandex.practicum.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

public class PostMapper implements RowMapper<Post> {
    @Override
    public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
        Post result = null;
        do {
            if (result == null) {
                result = new Post(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getString("content"),
                        rs.getInt("like_count"),
                        new LinkedHashSet<>(),
                        new LinkedHashSet<>()
                );
            }
            if (rs.getLong("tag_id") != 0) {
                Tag tag = new Tag(
                        rs.getLong("tag_id"),
                        rs.getString("tag_name")
                );
                result.getTags().add(tag);
            }
            if (rs.getLong("comment_id") != 0) {
                Comment comment = new Comment(
                        rs.getLong("comment_id"),
                        rs.getLong("id"),
                        rs.getString("comment_description")
                );
                result.getComments().add(comment);
            }
        } while (rs.next());

        return result;
    }
}
