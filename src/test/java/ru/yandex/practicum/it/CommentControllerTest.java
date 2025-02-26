package ru.yandex.practicum.it;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.repository.CommentRepository;
import ru.yandex.practicum.repository.PostRepository;
import ru.yandex.practicum.repository.TagRepository;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

        mockMvc.perform(multipart("/posts")
                        .file("image", "img1".getBytes())
                        .param("title", "Рецепт идеального чизкейка")
                        .param("content", "Чизкейк — это один из моих любимых десертов.")
                        .param("tags", "Рецепты, Десерты, Чизкейк"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void save_postIsPresent_shouldAddCommentToDatabase() throws Exception {
        mockMvc.perform(post("/posts/1/comments")
                        .param("description", "Новый комментарий"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(1L, "Рецепты"));
        tags.add(new Tag(2L, "Десерты"));
        tags.add(new Tag(3L, "Чизкейк"));

        Set<Comment> comments = new LinkedHashSet<>();
        comments.add(new Comment(1L, 1L, "Новый комментарий"));

        Post post = new Post(1L, "Рецепт идеального чизкейка", "aW1nMQ==",
                "Чизкейк — это один из моих любимых десертов.", 0,
                tags, comments
        );

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", post));
    }

    @Test
    void update_commentIsPresent_shouldUpdateCommentInDatabase() throws Exception {
        mockMvc.perform(post("/posts/1/comments")
                        .param("description", "Новый комментарий"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        mockMvc.perform(post("/posts/1/comments/1")
                        .param("_method", "put")
                        .param("description", "Обновленный комментарий"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(1L, "Рецепты"));
        tags.add(new Tag(2L, "Десерты"));
        tags.add(new Tag(3L, "Чизкейк"));

        Set<Comment> comments = new LinkedHashSet<>();
        comments.add(new Comment(1L, 1L, "Обновленный комментарий"));

        Post post = new Post(1L, "Рецепт идеального чизкейка", "aW1nMQ==",
                "Чизкейк — это один из моих любимых десертов.", 0,
                tags, comments
        );

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", post));
    }


    @Test
    void delete_commentIsPresent_shouldDeleteCommentFromDatabase() throws Exception {
        mockMvc.perform(post("/posts/1/comments")
                        .param("description", "Новый комментарий"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        mockMvc.perform(post("/posts/1/comments/1")
                .param("_method", "delete")
        ).andExpect(status().is3xxRedirection());

        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(1L, "Рецепты"));
        tags.add(new Tag(2L, "Десерты"));
        tags.add(new Tag(3L, "Чизкейк"));

        Set<Comment> comments = new LinkedHashSet<>();

        Post post = new Post(1L, "Рецепт идеального чизкейка", "aW1nMQ==",
                "Чизкейк — это один из моих любимых десертов.", 0,
                tags, comments
        );

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", post));
    }
}
