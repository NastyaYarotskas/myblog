package ru.yandex.practicum.repository;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.mapper.TagMapper;
import ru.yandex.practicum.model.Tag;

import java.util.List;
import java.util.Map;

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

    @Override
    public void deleteAll() {
        jdbcTemplate.update("delete from tags", Map.of());
        jdbcTemplate.update("ALTER TABLE tags ALTER COLUMN id RESTART WITH 1", Map.of());
    }
}
