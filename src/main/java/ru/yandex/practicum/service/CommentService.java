package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Comment;
import ru.yandex.practicum.repository.CommentRepository;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    public void update(Comment comment) {
        commentRepository.update(comment);
    }

    public void deleteById(Long postId, Long commentId) {
        commentRepository.deleteById(postId, commentId);
    }
}
