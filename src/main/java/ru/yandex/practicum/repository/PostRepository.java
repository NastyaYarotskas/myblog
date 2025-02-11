package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;

public interface PostRepository {
    Page<Post> findAll(int page, int size);

    Post findById(Long id);

    Page<Post> filterByTags(int page, int size, Long tagId);

    void save(Post post);

    void update(Post post);

    void deleteById(Long id);
}
