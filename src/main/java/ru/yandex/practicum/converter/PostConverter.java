package ru.yandex.practicum.converter;

import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.request.PostRequest;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class PostConverter {

    public static Post mapToPost(PostRequest request) {
        byte[] bytes = null;
        try {
            bytes = request.getImage().getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String imageBase64 = Base64.getEncoder().encodeToString(bytes);

        Optional.ofNullable(request.getTags()).orElse("");

        Set<Tag> tags = Arrays.stream(Optional.ofNullable(request.getTags()).orElse("").split(","))
                .toList().stream()
                .map(tag -> new Tag(0L, tag.trim()))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return new Post(
                0L,
                request.getTitle(),
                imageBase64,
                request.getContent(),
                0,
                tags,
                Set.of()
        );
    }
}
