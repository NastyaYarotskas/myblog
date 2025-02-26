package ru.yandex.practicum.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.repository.PostRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class JdbcNativePostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    void findById_postIsPresent_shouldAddUserToDatabase() {
        Post post = postRepository.findById(5L);

        assertNotNull(post);
        assertEquals(5L, post.getId());
    }

    @Test
    void findAll_postsArePresent_shouldReturnAllPost() {
        Page<Post> posts = postRepository.findAll(0, 5);

        assertNotNull(posts);
        assertFalse(posts.getCollection().isEmpty());
    }

    @Test
    void filterByTags_postWithTagIsPresent_shouldReturnAllPostWithRequiredTags() {
        Page<Post> posts = postRepository.filterByTags(0, 2, 13L);

        assertNotNull(posts);
        assertEquals(1, posts.getCollection().size());
        assertEquals(1, posts.getTotalPages(1));
        assertEquals(5, posts.getCollection().getFirst().getId());
    }

    @Test
    void save_paramsArePresent_shouldReturnAllPostWithRequiredTags() {
        postRepository.save(new Post(1L, "test", "test",
                "test", 1, Set.of(new Tag(1L, "tag")), Set.of()));

        Page<Post> posts = postRepository.findAll(0, 1);

        assertNotNull(posts);
        assertEquals("test", posts.getCollection().getFirst().getTitle());
    }

    @Test
    void update_paramsArePresent_shouldReturnAllPostWithRequiredTags() {
        postRepository.save(new Post(1L, "test", "test",
                "test", 1, Set.of(new Tag(1L, "tag")), Set.of()));

        Page<Post> posts = postRepository.findAll(0, 1);

        assertNotNull(posts);
        assertEquals("test", posts.getCollection().getFirst().getTitle());

        Post postToUpdate = posts.getCollection().getFirst();
        postToUpdate.setTitle("updated");

        postRepository.update(postToUpdate);

        posts = postRepository.findAll(0, 1);

        assertNotNull(posts);
        assertEquals("updated", posts.getCollection().getFirst().getTitle());
    }

    @Test
    void delete_postIsPresent_shouldReturnAllPostWithRequiredTags() {
        postRepository.save(new Post(1L, "test to delete", "test",
                "test", 1, Set.of(new Tag(1L, "tag")), Set.of()));

        Page<Post> posts = postRepository.findAll(0, 1);

        assertNotNull(posts);
        assertEquals("test to delete", posts.getCollection().getFirst().getTitle());

        Post postToDelete = posts.getCollection().getFirst();

        postRepository.deleteById(postToDelete.getId());

        posts = postRepository.findAll(0, 1);

        assertNotNull(posts);
        assertNotEquals("test to delete", posts.getCollection().getFirst().getTitle());
    }

    @Test
    void likePost_postIsPresent_shouldIncrementPostsLikeCount() {
        postRepository.save(new Post(1L, "test to increment", "test",
                "test", 1, Set.of(new Tag(1L, "tag")), Set.of()));

        Page<Post> posts = postRepository.findAll(0, 1);

        assertNotNull(posts);
        assertEquals("test to increment", posts.getCollection().getFirst().getTitle());

        Post postToLike = posts.getCollection().getFirst();

        postRepository.likePost(postToLike.getId());

        posts = postRepository.findAll(0, 1);

        assertNotNull(posts);
        assertEquals("test to increment", posts.getCollection().getFirst().getTitle());
        assertEquals(1, posts.getCollection().getFirst().getLikeCount());
    }
}
