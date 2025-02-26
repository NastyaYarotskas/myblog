package ru.yandex.practicum.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.TagRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
public class JdbcNativeCommentRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void cleanDatabase() throws Exception {
        tagRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
    }

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
