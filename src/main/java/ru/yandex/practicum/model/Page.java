package ru.yandex.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Page<T> {

    private List<T> collection;
    private int total;

    public int getTotalPages(int size) {
        return size == 0 ? 1 : (int) Math.ceil((double) total / (double) size);
    }
}
