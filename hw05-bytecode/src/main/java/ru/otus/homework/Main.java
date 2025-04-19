package ru.otus.homework;

import ru.otus.homework.proxy.Ioc;
import ru.otus.homework.proxy.TestLoggingInterface;

public class Main {
    public static void main(String[] args) {
        TestLoggingInterface myClass = Ioc.createClass();
        myClass.calculation(1, 2);
        myClass.calculation(5);
        myClass.calculation(10, 20, "30");
    }
}
