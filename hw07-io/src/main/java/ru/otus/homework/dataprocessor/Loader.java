package ru.otus.homework.dataprocessor;

import java.util.List;
import ru.otus.homework.model.Measurement;

public interface Loader {
    List<Measurement> load();
}
