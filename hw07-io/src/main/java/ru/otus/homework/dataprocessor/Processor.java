package ru.otus.homework.dataprocessor;

import java.util.List;
import java.util.Map;
import ru.otus.homework.model.Measurement;

public interface Processor {
    Map<String, Double> process(List<Measurement> data);
}
