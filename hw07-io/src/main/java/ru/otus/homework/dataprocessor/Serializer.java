package ru.otus.homework.dataprocessor;

import java.util.Map;

public interface Serializer {
    void serialize(Map<String, Double> data);
}
