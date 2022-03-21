package com.my_solution.sorftaria_test_task.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@Getter
@AllArgsConstructor
public class Pair<K, V> {
    private final K key;
    private final V value;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Pair<?, ?> objPair) {
            return objPair.getKey().equals(key) && objPair.getValue().equals(value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }
}

