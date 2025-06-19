package ru.otus.homework.services;

import java.util.List;
import ru.otus.homework.model.Equation;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
