package ru.otus.homework;

import ru.otus.homework.my_junit.After;
import ru.otus.homework.my_junit.Before;
import ru.otus.homework.my_junit.Test;

public class MyJunitTest {
    @After
    public void after1() {
        System.out.println("After1");
    }

    @After
    public void after2() {
        System.out.println("After2");
    }

    @Test
    public void test1() {
        System.out.println("test1");
    }

    @Test
    public void test2() {
        System.out.println("test2");
        throw new RuntimeException("Error");
    }

    @Before
    public void before1() {
        System.out.println("Before1");
    }

    @Before
    public void before2() {
        System.out.println("Before2");
    }
}
