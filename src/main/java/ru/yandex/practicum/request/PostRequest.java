package ru.yandex.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import ru.yandex.practicum.model.Tag;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostRequest {
    private String title;
    private MultipartFile image;
    private String content;
    private Set<Tag> tags;
}
