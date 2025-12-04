package ru.otus.homework.api.model;

import java.time.LocalDateTime;

public record SensorData(LocalDateTime measurementTime, String room, Double value) {}
