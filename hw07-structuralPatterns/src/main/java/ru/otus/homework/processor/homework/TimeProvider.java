package ru.otus.homework.processor.homework;

import java.time.LocalTime;

public interface TimeProvider {
    LocalTime getTime();
}
