package ru.otus.homework.lib;

import java.util.List;
import ru.otus.homework.api.model.SensorData;

public interface SensorDataBufferedWriter {
    void writeBufferedData(List<SensorData> bufferedData);
}
