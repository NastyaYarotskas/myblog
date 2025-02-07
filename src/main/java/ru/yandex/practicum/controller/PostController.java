package ru.yandex.practicum.controller;

import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.request.PostRequest;
import ru.yandex.practicum.service.PostService;

import java.util.List;

import static ru.yandex.practicum.mapper.PostMapper.mapToPost;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public String getAll(Model model) {
        List<Post> posts = service.findAll();
        model.addAttribute("posts", posts);
        model.addAttribute("count", posts.size());
        return "posts_page";
    }

    @GetMapping(value = "/{id}")
    public String getById(Model model, @PathVariable(name = "id") Long id) {
        Post post = service.findById(id);
        model.addAttribute("post", post);
        return "post_page";
    }

    @SneakyThrows
    @PostMapping
    public String save(@ModelAttribute PostRequest request) {
        Post post = mapToPost(request);
        service.save(post);
        return "redirect:/posts";
    }

    @SneakyThrows
    @PostMapping(value = "/{id}", params = "_method=put")
    public String updatePost(@PathVariable(name = "id") Long id, @ModelAttribute PostRequest request) {
        Post post = mapToPost(request);
        post.setId(id);
        service.updatePost(post);
        return "redirect:/posts/" + id;
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String delete(@PathVariable(name = "id") Long id) {
        service.deleteById(id);
        return "redirect:/posts";
    }
}
