package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Post;

import java.util.List;

public interface PostRepository {
    List<Post> findAll();

    void save(Post user);

    void deleteById(Long id);
}
