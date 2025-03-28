package ru.otus.homework;

import ru.otus.homework.my_junit.AnnotationProcessor;

public class Main {
    public static void main(String[] args) throws Exception {
        AnnotationProcessor.run(MyJunitTest.class);
    }
}
