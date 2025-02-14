package ru.yandex.practicum.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.configuration.DataSourceConfiguration;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.repository.JdbcNativePostRepository;
import ru.yandex.practicum.repository.PostRepository;

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
        Page<Post> posts = postRepository.findAll(0, 5);

        assertNotNull(posts);
    }

    @Test
    void filterByTags_shouldReturnAllPostWithRequiredTags() {
        Page<Post> posts = postRepository.filterByTags(0, 2, 13L);

        assertNotNull(posts);
        assertEquals(1, posts.getCollection().size());
        assertEquals(1, posts.getTotalPages(1));
    }

    @Test
    void save_shouldReturnAllPostWithRequiredTags() {
        postRepository.save(new Post(1L, "test", "test",
                "test", 1, Set.of(new Tag(1L, "tag")), Set.of()));

        Page<Post> posts = postRepository.findAll(0, 1);

        assertNotNull(posts);
    }
}
