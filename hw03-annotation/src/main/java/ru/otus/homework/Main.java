package ru.otus.homework;

import ru.otus.homework.my_junit.AnnotationProcessor;
import ru.otus.homework.my_junit.TestResult;

public class Main {
    public static void main(String[] args) throws Exception {
        TestResult testResult = AnnotationProcessor.run(MyJunitTest.class);
        testResult.printResultOfTestClass();
        TestResult testResult2 = AnnotationProcessor.run(MySecondJunitTest.class);
        testResult2.printResultOfTestClass();
    }
}
