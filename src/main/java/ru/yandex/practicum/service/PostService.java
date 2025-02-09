package ru.yandex.practicum.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

import java.util.List;
import java.util.Set;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }

    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> filterByTags(Set<Long> tags) {
        return postRepository.filterByTags(tags);
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
