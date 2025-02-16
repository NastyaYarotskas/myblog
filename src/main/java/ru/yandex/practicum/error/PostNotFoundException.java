package ru.yandex.practicum.error;

public class PostNotFoundException extends RuntimeException {
    public PostNotFoundException(Long id) {
        super("Пост с ID " + id + " не найден");
    }
}
