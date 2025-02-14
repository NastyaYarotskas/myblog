package ru.yandex.practicum.mapper;

import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.model.Tag;
import ru.yandex.practicum.request.PostRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class PostMapper {

    public static Post mapToPost(PostRequest request) throws IOException {
        byte[] bytes = request.getImage().getBytes();
        String imageBase64 = Base64.getEncoder().encodeToString(bytes);

        Optional.ofNullable(request.getTags()).orElse("");

        Set<Tag> tags = Arrays.stream(Optional.ofNullable(request.getTags()).orElse("").split(","))
                .toList().stream()
                .map(tag -> new Tag(0L, tag.trim()))
                .collect(Collectors.toSet());

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
