package ru.yandex.practicum.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Tag {
    private Long id;
    private String name;
}
