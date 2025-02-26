package ru.yandex.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.model.Page;
import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.repository.PostRepository;

@Service
public class PostService {

    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> findAll(int page, int size) {
        Page<Post> posts = postRepository.findAll(page, size);
        updateContent(posts);
        return posts;
    }

    public Post findById(Long id) {
        return postRepository.findById(id);
    }

    public Page<Post> filterByTags(int page, int size, Long tagId) {
        Page<Post> posts = postRepository.filterByTags(page, size, tagId);
        updateContent(posts);
        return posts;
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

    public void likePost(Long id) {
        postRepository.likePost(id);
    }

    private void updateContent(Page<Post> posts) {
        posts.getCollection()
                .forEach(post -> post.setContent(getShortContent(post)));
    }

    private String getShortContent(Post post) {
        String content = post.getContent();
        String shortContent = content.length() > 200 ? content.substring(0, 199) : content;
        return shortContent + "...";
    }
}
