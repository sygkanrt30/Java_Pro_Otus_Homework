package ru.otus.homework.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import lombok.AccessLevel;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Ioc {
    public static TestLoggingInterface createClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLogging());
        return (TestLoggingInterface) Proxy.newProxyInstance(
                Ioc.class.getClassLoader(), new Class<?>[] {TestLoggingInterface.class}, handler);
    }

    @ToString
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    static class DemoInvocationHandler implements InvocationHandler {
        TestLoggingInterface myClass;

        DemoInvocationHandler(TestLoggingInterface myClass) {
            this.myClass = myClass;
        }

        @Override
        @SneakyThrows
        public Object invoke(Object proxy, Method method, Object[] args) {
            if (isMethodHasAnnotation(method)) {
                log.info("executed method: {}, params: {}", method.getName(), args);
            }
            return method.invoke(myClass, args);
        }

        @SneakyThrows
        private boolean isMethodHasAnnotation(Method method) {
            Class<?> clazz = myClass.getClass();
            Method originMethod = clazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
            return originMethod.isAnnotationPresent(MyLog.class);
        }
    }
}
