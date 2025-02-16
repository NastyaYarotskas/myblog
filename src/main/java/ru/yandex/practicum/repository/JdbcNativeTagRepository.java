package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.model.Tag;

import java.util.List;

@Repository
public class JdbcNativeTagRepository implements TagRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcNativeTagRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> findAllTags() {
        return jdbcTemplate.query(
                """
                        select id, name
                        from tags
                        """,
                new TagMapper()
        );
    }
}
