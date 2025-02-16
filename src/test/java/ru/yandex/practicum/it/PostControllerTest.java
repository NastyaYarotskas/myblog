package ru.yandex.practicum.it;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
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

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfiguration.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @Order(1)
    void getPostControllerBean_wacIsAutowired_shouldProvidePostController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertNotNull(webApplicationContext.getBean("postController"));
    }

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
