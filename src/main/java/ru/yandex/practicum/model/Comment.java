package ru.yandex.practicum.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Comment {
    private Long id;
    private Long postId;
    private String description;
}
