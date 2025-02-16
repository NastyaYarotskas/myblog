package ru.yandex.practicum.mapper;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.model.Tag;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagMapper implements ResultSetExtractor<List<Tag>> {
    @Override
    public List<Tag> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Tag> result = new ArrayList<>();
        while (rs.next()) {
            Tag tag = new Tag(
                    rs.getLong("id"),
                    rs.getString("name")
            );
            result.add(tag);
        }

        return result;
    }
}
