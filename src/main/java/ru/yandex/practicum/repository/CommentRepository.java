package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Comment;

public interface CommentRepository {

    void save(Comment comment);

    void update(Comment comment);

    void deleteById(Long postId, Long commentId);
}
