package ru.otus.homework.dataprocessor;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.otus.homework.model.Measurement;

@Slf4j
@SuppressWarnings("All")
public class ResourcesFileLoader implements Loader {
    private final String fileName;
    private final Gson gson;

    public ResourcesFileLoader(String fileName) {
        this.fileName = fileName;
        this.gson = new Gson();
    }

    @Override
    public List<Measurement> load() {
        String fileContent = readFile();
        return makeListWithMeasurement(fileContent);
    }

    private String readFile() {
        try (var stream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            return makeStringFromStream(stream);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileProcessException(e);
        }
    }

    private String makeStringFromStream(InputStream stream) throws IOException {
        byte[] buffer = stream.readAllBytes();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    private List<Measurement> makeListWithMeasurement(String fileContent) {
        val list = new ArrayList<Measurement>();
        fileContent = fileContent.replaceAll("[\\[\\]]", "");
        for (String measurementStr : fileContent.split("\\},\\s*\\{")) {
            measurementStr = measurementStr.replaceAll("^\\{", "").replaceAll("\\}$", "");
            measurementStr = "{" + measurementStr + "}";
            Measurement measurement = gson.fromJson(measurementStr, Measurement.class);
            list.add(measurement);
        }
        return list;
    }
}
