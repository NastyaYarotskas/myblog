package ru.yandex.practicum.it;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.WebConfiguration;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CommentControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @Order(1)
    void save_postIsPresent_shouldAddCommentToDatabase() throws Exception {
        mockMvc.perform(post("/posts/2/comments")
                        .param("description", "Новый комментарий"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/2"));

        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(4L, "Книги"));
        tags.add(new Tag(5L, "Саморазвитие"));
        tags.add(new Tag(6L, "Мотивация"));

        Set<Comment> comments = new LinkedHashSet<>();
        comments.add(new Comment(3L, 2L, "Спасибо за подборку! Уже прочитала две книги из списка, и они действительно вдохновляют."));
        comments.add(new Comment(4L, 2L, "А есть ли у вас рекомендации по книгам о финансовой грамотности?"));
        comments.add(new Comment(11L, 2L, "Новый комментарий"));

        Post post = new Post(2L, "Топ-5 книг для саморазвития", "img2",
                "Книги — это мощный инструмент для саморазвития.", 10,
                tags, comments
        );

        mockMvc.perform(get("/posts/2"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", post));

        mockMvc.perform(post("/posts/2/comments/11")
                .param("_method", "delete")
        ).andExpect(status().is3xxRedirection());
    }

    @Test
    @Order(2)
    void update_commentIsPresent_shouldUpdateCommentInDatabase() throws Exception {
        mockMvc.perform(post("/posts/2/comments/3")
                        .param("_method", "put")
                        .param("description", "Обновленный комментарий"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/2"));

        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(4L, "Книги"));
        tags.add(new Tag(5L, "Саморазвитие"));
        tags.add(new Tag(6L, "Мотивация"));

        Set<Comment> comments = new LinkedHashSet<>();
        comments.add(new Comment(3L, 2L, "Обновленный комментарий"));
        comments.add(new Comment(4L, 2L, "А есть ли у вас рекомендации по книгам о финансовой грамотности?"));

        Post post = new Post(2L, "Топ-5 книг для саморазвития", "img2",
                "Книги — это мощный инструмент для саморазвития.", 10,
                tags, comments
        );

        mockMvc.perform(get("/posts/2"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", post));

        mockMvc.perform(post("/posts/2/comments/3")
                .param("_method", "put")
                .param("description", "Спасибо за подборку! Уже прочитала две книги из списка, и они действительно вдохновляют."));
    }


    @Test
    @Order(3)
    void delete_commentIsPresent_shouldDeleteCommentFromDatabase() throws Exception {
        mockMvc.perform(post("/posts/2/comments/3")
                .param("_method", "delete")
        ).andExpect(status().is3xxRedirection());

        LinkedHashSet<Tag> tags = new LinkedHashSet<>();
        tags.add(new Tag(4L, "Книги"));
        tags.add(new Tag(5L, "Саморазвитие"));
        tags.add(new Tag(6L, "Мотивация"));

        Set<Comment> comments = new LinkedHashSet<>();
        comments.add(new Comment(4L, 2L, "А есть ли у вас рекомендации по книгам о финансовой грамотности?"));

        Post post = new Post(2L, "Топ-5 книг для саморазвития", "img2",
                "Книги — это мощный инструмент для саморазвития.", 10,
                tags, comments
        );

        mockMvc.perform(get("/posts/2"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("post"))
                .andExpect(model().attribute("post", post));
    }
}
