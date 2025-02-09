package ru.yandex.practicum.repository;

import ru.yandex.practicum.model.Tag;

import java.util.List;

public interface TagRepository {

    List<Tag> findAllTags();
}
