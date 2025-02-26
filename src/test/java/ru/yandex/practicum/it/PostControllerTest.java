package ru.yandex.practicum.it;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(2)
    void getAll_postsExists_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts_page"))
                .andExpect(model().attributeExists("posts"));
    }

    @Test
    @Order(3)
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
    @Order(4)
    void save_paramsArePresent_shouldAddPostToDatabase() throws Exception {
        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(15L, "tag1"));
        tags.add(new Tag(16L, "tag2"));
        tags.add(new Tag(17L, "tag3"));

        Set<Comment> comments = new LinkedHashSet<>();

        Post post = new Post(6L, "title", "aW1n",
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

        mockMvc.perform(get("/posts/6"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("post", post));

        mockMvc.perform(post("/posts/6")
                .param("_method", "delete"));
    }

    @Test
    @Order(5)
    void getPostById_postIsPresent_shouldReturnCorrectPost() throws Exception {
        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(1L, "Рецепты"));
        tags.add(new Tag(2L, "Десерты"));
        tags.add(new Tag(3L, "Чизкейк"));

        Set<Comment> comments = new LinkedHashSet<>();
        comments.add(new Comment(1L, 1L, "Спасибо за рецепт! Попробовала сделать, получилось очень вкусно. Теперь это мой любимый десерт."));
        comments.add(new Comment(2L, 1L, "А можно ли заменить творожный сыр на что-то другое? У меня на него аллергия."));

        Post post = new Post(1L, "Рецепт идеального чизкейка", "img1",
                "Чизкейк — это один из моих любимых десертов.", 20,
                tags, comments
        );

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("post", post));
    }

    @Test
    @Order(6)
    void updatePost_postIsPresent_shouldUpdatePostInDatabase() throws Exception {
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
        tags.add(new Tag(18L, "Торт"));

        Set<Comment> comments = new LinkedHashSet<>();
        comments.add(new Comment(1L, 1L, "Спасибо за рецепт! Попробовала сделать, получилось очень вкусно. Теперь это мой любимый десерт."));
        comments.add(new Comment(2L, 1L, "А можно ли заменить творожный сыр на что-то другое? У меня на него аллергия."));

        Post post = new Post(1L, "Новый пост", "aW1n",
                "Содержание поста", 20,
                tags, comments
        );

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("post", post));
    }

    @Test
    @Order(7)
    void deleteById_postIsPresent_shouldRemovePostFromDatabaseAndRedirect() throws Exception {
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
