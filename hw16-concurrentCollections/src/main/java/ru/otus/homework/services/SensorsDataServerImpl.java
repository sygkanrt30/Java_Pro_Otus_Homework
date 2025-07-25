package ru.otus.homework.services;

import lombok.RequiredArgsConstructor;
import ru.otus.homework.api.SensorsDataChannel;
import ru.otus.homework.api.SensorsDataServer;
import ru.otus.homework.api.model.SensorData;

@RequiredArgsConstructor
public class SensorsDataServerImpl implements SensorsDataServer {
    private final SensorsDataChannel sensorsDataChannel;

    @Override
    public void onReceive(SensorData sensorData) {
        sensorsDataChannel.push(sensorData);
    }
}
