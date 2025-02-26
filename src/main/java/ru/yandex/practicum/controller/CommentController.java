package ru.yandex.practicum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.service.CommentService;

@Controller
@RequestMapping("/posts/{post-id}/comments")
public class CommentController {

    private final CommentService commentService;

    //    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public String save(@PathVariable(name = "post-id") Long postId, @ModelAttribute Comment comment) {
        comment.setPostId(postId);
        commentService.save(comment);
        return "redirect:/posts/" + postId;
    }

    @PostMapping(value = "/{comment-id}", params = "_method=put")
    public String update(@PathVariable(name = "post-id") Long postId,
                         @PathVariable(name = "comment-id") Long commentId,
                         @ModelAttribute Comment comment) {
        comment.setId(commentId);
        comment.setPostId(postId);
        commentService.update(comment);
        return "redirect:/posts/" + postId;
    }

    @PostMapping(value = "/{comment-id}", params = "_method=delete")
    public String delete(@PathVariable(name = "post-id") Long postId,
                         @PathVariable(name = "comment-id") Long commentId) {
        commentService.deleteById(postId, commentId);
        return "redirect:/posts/" + postId;
    }
}
