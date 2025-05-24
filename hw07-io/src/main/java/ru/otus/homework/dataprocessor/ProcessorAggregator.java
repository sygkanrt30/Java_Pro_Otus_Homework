package ru.otus.homework.dataprocessor;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import ru.otus.homework.model.Measurement;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> map = data.stream()
                .collect(Collectors.groupingBy(Measurement::name, Collectors.summingDouble(Measurement::value)));
        return new TreeMap<>(map);
    }
}
