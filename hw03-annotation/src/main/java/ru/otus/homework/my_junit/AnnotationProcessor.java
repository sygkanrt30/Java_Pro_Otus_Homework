package ru.otus.homework.my_junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

public class AnnotationProcessor {
    private static final Set<Method> AFTER_METHODS = new HashSet<>();
    private static final Set<Method> BEFORE_METHODS = new HashSet<>();
    private static final Set<Method> TEST_METHODS = new HashSet<>();
    private static int COUNT_NOT_PASSED_TEST_METHODS = 0;

    public static TestResult run(Class<?> clazz) throws Exception {
        groupMethodsByAnnotations(clazz);
        executeLifeCycle(clazz);
        TestResult testResult = new TestResult(TEST_METHODS.size(), COUNT_NOT_PASSED_TEST_METHODS);
        resetField();
        return testResult;
    }

    private static void executeLifeCycle(Class<?> clazz) throws Exception {
        for (Method method : TEST_METHODS) {
            Constructor<?> constructor = clazz.getConstructor();
            Object testClass = constructor.newInstance();
            executeBeforeAndAfterEach(BEFORE_METHODS, testClass);
            executeTestMethod(method, testClass, true);
            executeBeforeAndAfterEach(AFTER_METHODS, testClass);
        }
    }

    private static void resetField() {
        AFTER_METHODS.clear();
        BEFORE_METHODS.clear();
        TEST_METHODS.clear();
        COUNT_NOT_PASSED_TEST_METHODS = 0;
    }

    private static void executeTestMethod(Method method, Object testClass, boolean isTest) {
        try {
            method.invoke(testClass);
        } catch (Exception e) {
            if (isTest) COUNT_NOT_PASSED_TEST_METHODS++;
        }
    }

    private static void groupMethodsByAnnotations(Class<?> clazz) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                BEFORE_METHODS.add(method);
            } else if (method.isAnnotationPresent(After.class)) {
                AFTER_METHODS.add(method);
            } else if (method.isAnnotationPresent(Test.class)) {
                TEST_METHODS.add(method);
            }
        }
    }

    private static void executeBeforeAndAfterEach(Set<Method> methods, Object clazz) {
        for (Method method : methods) {
            executeTestMethod(method, clazz, false);
        }
    }
}
