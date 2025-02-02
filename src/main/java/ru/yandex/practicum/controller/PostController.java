package ru.yandex.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.service.PostService;

import java.util.List;

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

    @PostMapping
    public String save(@ModelAttribute Post post) {
        service.save(post);
        return "redirect:/posts";
    }

    @PostMapping(value = "/{id}", params = "_method=delete")
    public String delete(@PathVariable(name = "id") Long id) {
        service.deleteById(id);
        return "redirect:/posts";
    }
}
