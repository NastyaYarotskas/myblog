package ru.yandex.practicum.it;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import ru.yandex.practicum.configuration.DataSourceConfiguration;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.JdbcNativeCommentRepository;
import ru.yandex.practicum.repository.JdbcNativePostRepository;
import ru.yandex.practicum.repository.PostRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(classes = {DataSourceConfiguration.class, JdbcNativePostRepository.class, JdbcNativeCommentRepository.class})
@TestPropertySource(locations = "classpath:test-application.properties")
public class JdbcNativeCommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    void save_postIsPresent_shouldSaveCommentToDatabase() {
        postRepository.save(new Post(1L, "test", "test",
                "test", 1, Set.of(new Tag(1L, "tag")), Set.of()));

        Page<Post> posts = postRepository.findAll(0, 1);

        commentRepository.save(new Comment(0L, posts.getCollection().getFirst().getId(), "new comment"));

        Post post = postRepository.findById(posts.getCollection().getFirst().getId());

        assertNotNull(post);
        assertEquals("new comment", post.getComments().stream().iterator().next().getDescription());
    }

    @Test
    void update_postIsPresent_shouldUpdateCommentInDatabase() {
        postRepository.save(new Post(1L, "test", "test",
                "test", 1, Set.of(new Tag(1L, "tag")), Set.of()));

        Page<Post> posts = postRepository.findAll(0, 1);

        commentRepository.save(new Comment(0L, posts.getCollection().getFirst().getId(), "new comment"));

        Post post = postRepository.findById(posts.getCollection().getFirst().getId());

        assertNotNull(post);
        assertEquals("new comment", post.getComments().stream().iterator().next().getDescription());

        Comment comment = post.getComments().stream().iterator().next();
        comment.setDescription("updated comment");

        commentRepository.update(comment);

        post = postRepository.findById(posts.getCollection().getFirst().getId());

        assertNotNull(post);
        assertEquals("updated comment", post.getComments().stream().iterator().next().getDescription());
    }

    @Test
    void delete_postIsPresent_shouldDeleteCommentInDatabase() {
        postRepository.save(new Post(1L, "test", "test",
                "test", 1, Set.of(new Tag(1L, "tag")), Set.of()));

        Page<Post> posts = postRepository.findAll(0, 1);

        commentRepository.save(new Comment(0L, posts.getCollection().getFirst().getId(), "new comment"));

        Post post = postRepository.findById(posts.getCollection().getFirst().getId());

        assertNotNull(post);
        assertEquals("new comment", post.getComments().stream().iterator().next().getDescription());

        commentRepository.deleteById(post.getId(), post.getComments().stream().iterator().next().getId());

        post = postRepository.findById(post.getId());

        assertNotNull(post);
        assertEquals(0, post.getComments().size());
    }
}
