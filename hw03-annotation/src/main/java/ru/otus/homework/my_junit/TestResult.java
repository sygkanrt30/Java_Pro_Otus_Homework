package ru.otus.homework.my_junit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestResult {
    private static final Logger log = LoggerFactory.getLogger(TestResult.class);
    private final int countTestMethods;
    private final int countNotPassedTestMethods;

    public TestResult(int countTestMethods, int countPassedTestMethods) {
        this.countTestMethods = countTestMethods;
        this.countNotPassedTestMethods = countPassedTestMethods;
    }

    public void printResultOfTestClass() {
        log.info(
                "Total test methods: {}; Passed test methods: {}; Failed test methods: {}",
                countTestMethods,
                countTestMethods - countNotPassedTestMethods,
                countNotPassedTestMethods);
    }
}
