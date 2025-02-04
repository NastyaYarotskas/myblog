package ru.yandex.practicum.it;

import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {WebConfiguration.class})
class PostControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void getPostControllerBean_wacIsAutowired_shouldProvidePostController() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertNotNull(servletContext);
        assertInstanceOf(MockServletContext.class, servletContext);
        assertNotNull(webApplicationContext.getBean("postController"));
    }

    @Test
    void getAll_postsExists_shouldReturnHtmlWithPosts() throws Exception {
        mockMvc.perform(get("/posts"))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/html;charset=UTF-8"))
                .andExpect(view().name("posts_page"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(xpath("//body/div/div[2]/h2").string("Путешествие в горы: впечатления и советы"))
                .andExpect(xpath("//body/div/div[3]/h2").string("Как выбрать ноутбук для работы и учёбы"));
    }

    @Test
    void save_paramsArePresent_shouldAddPostToDatabaseAndRedirect() throws Exception {
        mockMvc.perform(post("/posts")
                        .param("title", "Новый пост")
                        .param("image", "img")
                        .param("content", "Содержание поста"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void deleteById_postIsPresent_shouldRemovePostFromDatabaseAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1")
                        .param("_method", "delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void updatePost_postIsPresent_shouldUpdatePostFromDatabaseAndRedirect() throws Exception {
        mockMvc.perform(post("/posts/1")
                        .param("_method", "put")
                        .param("title", "Новый пост")
                        .param("image", "img")
                        .param("content", "Содержание поста")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }
}
