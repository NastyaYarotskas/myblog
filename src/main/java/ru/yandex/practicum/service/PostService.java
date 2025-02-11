package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> findAll(int page, int size) {
        return postRepository.findAll(page, size);
    }

    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    public Page<Post> filterByTags(int page, int size, Long tagId) {
        return postRepository.filterByTags(page, size, tagId);
    }

    public void save(Post post) {
        postRepository.save(post);
    }

    public void deleteById(Long id) {
        postRepository.deleteById(id);
    }

    public void updatePost(Post post) {
        postRepository.update(post);
    }
}
