package ru.otus.homework.api;

import java.util.concurrent.TimeUnit;
import ru.otus.homework.api.model.SensorData;

public interface SensorsDataChannel {
    boolean push(SensorData sensorData);

    boolean isEmpty();

    SensorData take(long timeout, TimeUnit unit) throws InterruptedException;
}
