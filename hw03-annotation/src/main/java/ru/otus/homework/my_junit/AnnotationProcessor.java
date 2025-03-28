package ru.otus.homework.my_junit;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.MyJunitTest;

public class AnnotationProcessor {
    private static final Logger log = LoggerFactory.getLogger(AnnotationProcessor.class);
    private static final Set<Method> AFTER_METHODS = new HashSet<>();
    private static final Set<Method> BEFORE_METHODS = new HashSet<>();
    private static final Set<Method> TEST_METHODS = new HashSet<>();
    private static int countNotPassedTestMethod = 0;

    public static void run(Class<MyJunitTest> clazz) throws Exception {
        groupMethodsByAnnotations(clazz);
        executeLifeCycle(clazz);
        log.info(
                "Total test methods: {}; Passed test methods: {}; Failed test methods: {}",
                TEST_METHODS.size(),
                TEST_METHODS.size() - countNotPassedTestMethod,
                countNotPassedTestMethod);
    }

    private static void executeLifeCycle(Class<MyJunitTest> clazz) throws Exception {
        for (Method method : TEST_METHODS) {
            Constructor<MyJunitTest> constructor = clazz.getConstructor();
            MyJunitTest testClass = constructor.newInstance();
            executeBeforeAndAfter(BEFORE_METHODS, testClass);
            executeTestMethod(method, testClass);
            executeBeforeAndAfter(AFTER_METHODS, testClass);
        }
    }

    private static void executeTestMethod(Method method, MyJunitTest testClass) {
        try {
            method.invoke(testClass);
        } catch (Exception e) {
            countNotPassedTestMethod++;
        }
    }

    private static void groupMethodsByAnnotations(Class<MyJunitTest> clazz) {
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

    private static void executeBeforeAndAfter(Set<Method> methods, MyJunitTest clazz) throws Exception {
        for (Method method : methods) {
            method.invoke(clazz);
        }
    }
}
