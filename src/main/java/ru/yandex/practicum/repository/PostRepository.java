package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Post;

import java.util.List;
import java.util.Set;

public interface PostRepository {
    List<Post> findAll();

    Post findById(Long id);

    List<Post> filterByTags(Set<Long> tags);

    void save(Post post);

    void update(Post post);

    void deleteById(Long id);
}
