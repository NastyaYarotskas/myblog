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
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void cleanDatabase() {
        tagRepository.deleteAll();
        commentRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    void getAll_postsExists_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts_page"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    void getAll_withTag_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/posts")
                        .param("tag", "13")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts_page"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    void save_paramsArePresent_shouldAddPostToDatabase() throws Exception {
        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(1L, "tag1"));
        tags.add(new Tag(2L, "tag2"));
        tags.add(new Tag(3L, "tag3"));

        Set<Comment> comments = new LinkedHashSet<>();

        Post post = new Post(1L, "title", "aW1n",
                "content", 0,
                tags, comments
        );

        mockMvc.perform(multipart("/posts")
                        .file("image", "img".getBytes())
                        .param("title", "title")
                        .param("content", "content")
                        .param("tags", "tag1, tag2, tag3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("post", post));
    }

    @Test
    void getPostById_postIsPresent_shouldReturnCorrectPost() throws Exception {
        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(1L, "Рецепты"));
        tags.add(new Tag(2L, "Десерты"));
        tags.add(new Tag(3L, "Чизкейк"));

        Set<Comment> comments = new LinkedHashSet<>();
        comments.add(new Comment(1L, 1L, "Спасибо за рецепт! Попробовала сделать, получилось очень вкусно. Теперь это мой любимый десерт."));
        comments.add(new Comment(2L, 1L, "А можно ли заменить творожный сыр на что-то другое? У меня на него аллергия."));

        Post post = new Post(1L, "Рецепт идеального чизкейка", "aW1nMQ==",
                "Чизкейк — это один из моих любимых десертов.", 0,
                tags, comments
        );

        mockMvc.perform(multipart("/posts")
                        .file("image", "img1".getBytes())
                        .param("title", "Рецепт идеального чизкейка")
                        .param("content", "Чизкейк — это один из моих любимых десертов.")
                        .param("tags", "Рецепты, Десерты, Чизкейк"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        mockMvc.perform(post("/posts/1/comments")
                        .param("description", "Спасибо за рецепт! Попробовала сделать, получилось очень вкусно. Теперь это мой любимый десерт."))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/posts/1/comments")
                        .param("description", "А можно ли заменить творожный сыр на что-то другое? У меня на него аллергия."))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("post", post));
    }

    @Test
    void updatePost_postIsPresent_shouldUpdatePostInDatabase() throws Exception {
        mockMvc.perform(multipart("/posts")
                        .file("image", "img1".getBytes())
                        .param("title", "Рецепт идеального чизкейка")
                        .param("content", "Чизкейк — это один из моих любимых десертов.")
                        .param("tags", "Рецепты, Десерты, Чизкейк"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        mockMvc.perform(post("/posts/1/comments")
                        .param("description", "Спасибо за рецепт! Попробовала сделать, получилось очень вкусно. Теперь это мой любимый десерт."))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/posts/1/comments")
                        .param("description", "А можно ли заменить творожный сыр на что-то другое? У меня на него аллергия."))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(multipart("/posts/1")
                        .file("image", "img".getBytes())
                        .param("_method", "put")
                        .param("title", "Новый пост")
                        .param("content", "Содержание поста")
                        .param("tags", "Рецепты, Десерты, Торт")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));

        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(1L, "Рецепты"));
        tags.add(new Tag(2L, "Десерты"));
        tags.add(new Tag(4L, "Торт"));

        Set<Comment> comments = new LinkedHashSet<>();
        comments.add(new Comment(1L, 1L, "Спасибо за рецепт! Попробовала сделать, получилось очень вкусно. Теперь это мой любимый десерт."));
        comments.add(new Comment(2L, 1L, "А можно ли заменить творожный сыр на что-то другое? У меня на него аллергия."));

        Post post = new Post(1L, "Новый пост", "aW1n",
                "Содержание поста", 0,
                tags, comments
        );

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("post", post));
    }

    @Test
    void deleteById_postIsPresent_shouldRemovePostFromDatabaseAndRedirect() throws Exception {
        mockMvc.perform(multipart("/posts")
                        .file("image", "img1".getBytes())
                        .param("title", "Рецепт идеального чизкейка")
                        .param("content", "Чизкейк — это один из моих любимых десертов.")
                        .param("tags", "Рецепты, Десерты, Чизкейк"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        mockMvc.perform(post("/posts/1")
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(xpath("//div/p").string("Пост с ID 1 не найден"));
    }
}
