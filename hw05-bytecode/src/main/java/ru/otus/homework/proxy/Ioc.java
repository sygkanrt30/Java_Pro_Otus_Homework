package ru.otus.homework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Ioc {
    public static TestLoggingInterface createClass() {
        InvocationHandler handler = new DemoInvocationHandler<>(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    @ToString
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    static class DemoInvocationHandler<T> implements InvocationHandler {
        T myClass;
        List<Method> methodsToLog;

        DemoInvocationHandler(T myClass) {
            this.myClass = myClass;
            this.methodsToLog = parceMethodToLog(myClass);
        }

        private List<Method> parceMethodToLog(T myClass) {
            List<Method> methods = new ArrayList<>();
            Class<?> clazz = myClass.getClass();
            for (Method method : clazz.getMethods()) {
                if (isMethodHasAnnotation(method)) {
                    methods.add(method);
                }
            }
            return methods;
        }

        @SneakyThrows
        private boolean isMethodHasAnnotation(Method method) {
            return method.isAnnotationPresent(Log.class);
        }

        @Override
        @SneakyThrows
        public Object invoke(Object proxy, Method method, Object[] args) {
            if (isMethodToLog(method)) {
                log.info("executed method: {}, params: {}", method.getName(), args);
            }
            return method.invoke(myClass, args);
        }

        private boolean isMethodToLog(Method method) {
            for (Method m : methodsToLog) {
                String methodName = m.getName();
                Class<?>[] parameterTypes = m.getParameterTypes();
                if (isMethodContainInList(method, methodName, parameterTypes)) return true;
            }
            return false;
        }

        private boolean isMethodContainInList(Method method, String methodName, Class<?>[] parameterTypes) {
            return methodName.equals(method.getName()) && Arrays.equals(parameterTypes, method.getParameterTypes());
        }
    }
}
