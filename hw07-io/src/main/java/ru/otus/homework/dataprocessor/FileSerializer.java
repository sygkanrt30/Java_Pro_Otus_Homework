package ru.otus.homework.dataprocessor;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
        String content = makeContentFromMap(data);
        writeToFile(content);
    }

    private String makeContentFromMap(Map<String, Double> data) {
        StringBuilder content = new StringBuilder("{");
        for (Map.Entry<String, Double> entry : data.entrySet()) {
            content.append("\"")
                    .append(entry.getKey())
                    .append("\":")
                    .append(entry.getValue())
                    .append(",");
        }
        content.deleteCharAt(content.length() - 1);
        content.append("}");
        return content.toString();
    }

    private void writeToFile(String content) {
        try (var writer = new FileWriter(fileName)) {
            writer.write(content);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileProcessException(e);
        }
    }
}
