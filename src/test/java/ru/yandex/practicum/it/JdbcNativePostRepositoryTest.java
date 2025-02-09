package ru.yandex.practicum.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.configuration.DataSourceConfiguration;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.JdbcNativePostRepository;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcNativePostRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativePostRepositoryTest {

    @Autowired
    private NamedParameterJdbcTemplate namedJdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @Test
    void findById_shouldAddUserToDatabase() {
        Post post = postRepository.findById(1L);

        assertNotNull(post);
    }

    @Test
    void findAll_shouldAddUserToDatabase() {
        List<Post> posts = postRepository.findAll();

        assertNotNull(posts);
    }

    @Test
    void filterByTags_shouldReturnAllPostWithRequiredTags() {
        List<Post> posts = postRepository.filterByTags(Set.of(1L, 13L));

        assertNotNull(posts);
        assertEquals(2, posts.size());
    }
}
