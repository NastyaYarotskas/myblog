package ru.yandex.practicum.controller;

import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.request.PostRequest;
import ru.yandex.practicum.service.PostService;
import ru.yandex.practicum.service.TagService;

import static ru.yandex.practicum.converter.PostConverter.mapToPost;

@Controller
@RequestMapping("/posts")
public class PostController {

    private final PostService service;
    private final TagService tagService;

    //    @Autowired
    public PostController(PostService service, TagService tagService) {
        this.service = service;
        this.tagService = tagService;
    }

    @GetMapping
    public String getAll(Model model,
                         @RequestParam(value = "tag", required = false) Long tagId,
                         @RequestParam(value = "page", defaultValue = "0") int page,
                         @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        Page<Post> posts;
        if (tagId != null) {
            posts = service.filterByTags(page, size, tagId);
        } else {
            posts = service.findAll(page, size);
        }

        model.addAttribute("posts", posts.getCollection());
        model.addAttribute("totalPages", posts.getTotalPages(size));
        model.addAttribute("currentPage", page);
        model.addAttribute("allTags", tagService.findAllTags());

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

    @PostMapping(value = "/{id}/like")
    public String likePost(@PathVariable(name = "id") Long id) {
        service.likePost(id);
        return "redirect:/posts/" + id;
    }
}
