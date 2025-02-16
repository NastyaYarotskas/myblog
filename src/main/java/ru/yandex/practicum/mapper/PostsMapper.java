package ru.yandex.practicum.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class PostsMapper implements ResultSetExtractor<Page<Post>> {

    @Override
    public Page<Post> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Long, Post> result = new HashMap<>();
        int total = 0;
        while (rs.next()) {
            if (rs.isFirst()) {
                total = rs.getInt("total");
            }
            Post post = new Post(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("image"),
                    rs.getString("content"),
                    rs.getInt("like_count"),
                    new LinkedHashSet<>(),
                    new LinkedHashSet<>()
            );
            result.putIfAbsent(rs.getLong("id"), post);
            if (rs.getLong("tag_id") != 0) {
                Tag tag = new Tag(
                        rs.getLong("tag_id"),
                        rs.getString("tag_name")
                );
                result.get(rs.getLong("id")).getTags().add(tag);
            }
            if (rs.getLong("comment_id") != 0) {
                Comment comment = new Comment(
                        rs.getLong("comment_id"),
                        rs.getLong("id"),
                        rs.getString("comment_description")
                );
                result.get(rs.getLong("id")).getComments().add(comment);
            }
        }

        List<Post> posts = result.values().stream().sorted((o1, o2) -> o2.getId().compareTo(o1.getId())).toList();
        return new Page<>(posts, total);
    }
}
