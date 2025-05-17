package ru.otus.homework.dataprocessor;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@AllArgsConstructor
public class FileSerializer implements Serializer {
    private final String fileName;

    @Override
    @SneakyThrows
    public void serialize(Map<String, Double> data) {
        var mapper = new ObjectMapper();
        var file = new File(fileName);
        mapper.writeValue(file, data);
    }
}
