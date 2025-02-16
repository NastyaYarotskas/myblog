package ru.yandex.practicum.model;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Post {
    private Long id;
    private String title;
    private String image;
    private String content;
    private int likeCount;
    private Set<Tag> tags;
    private Set<Comment> comments;
}
