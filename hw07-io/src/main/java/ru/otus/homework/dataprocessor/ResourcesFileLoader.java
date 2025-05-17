package ru.otus.homework.dataprocessor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URL;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import ru.otus.homework.model.Measurement;

@AllArgsConstructor
public class ResourcesFileLoader implements Loader {
    private final String fileName;

    @Override
    @SneakyThrows
    public List<Measurement> load() {
        URL file = getClass().getClassLoader().getResource(fileName);
        var mapper = new ObjectMapper();
        return mapper.readValue(file, new TypeReference<>() {});
    }
}
