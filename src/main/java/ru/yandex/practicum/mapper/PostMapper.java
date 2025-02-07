package ru.yandex.practicum.mapper;

import ru.yandex.practicum.model.Post;
import ru.yandex.practicum.request.PostRequest;

import java.io.IOException;
import java.util.Base64;
import java.util.Set;

public class PostMapper {

    public static Post mapToPost(PostRequest request) throws IOException {
        byte[] bytes = request.getImage().getBytes();
        String imageBase64 = Base64.getEncoder().encodeToString(bytes);

        return new Post(
                0L,
                request.getTitle(),
                imageBase64,
                request.getContent(),
                0,
                request.getTags(),
                Set.of()
        );
    }
}
