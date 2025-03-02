package ru.otus.java;

import com.google.common.base.Joiner;

public class HelloOtus {
    public static void main(String[] args) {
        String result = Joiner.on(", ").join("Demo", "Otus");
        System.out.println(result);
    }
}