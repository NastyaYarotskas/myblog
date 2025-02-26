package ru.yandex.practicum.request;

import org.springframework.web.multipart.MultipartFile;

public class PostRequest {
    private String title;
    private MultipartFile image;
    private String content;
    private String tags;

    public PostRequest() {
    }

    public PostRequest(String title, MultipartFile image, String content, String tags) {
        this.title = title;
        this.image = image;
        this.content = content;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public MultipartFile getImage() {
        return image;
    }

    public String getContent() {
        return content;
    }

    public String getTags() {
        return tags;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
