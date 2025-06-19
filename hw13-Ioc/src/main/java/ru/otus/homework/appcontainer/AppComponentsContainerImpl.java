package ru.otus.homework.appcontainer;

import java.lang.reflect.Method;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.appcontainer.api.AppComponent;
import ru.otus.homework.appcontainer.api.AppComponentsContainer;
import ru.otus.homework.appcontainer.api.AppComponentsContainerConfig;

@SuppressWarnings("squid:S1068")
@Slf4j
public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        var comparator = getClassComparator();
        Arrays.stream(initialConfigClasses).sorted(comparator).forEach(this::processConfig);
    }

    private Comparator<Class<?>> getClassComparator() {
        return (o1, o2) -> {
            int order1 =
                    o1.getAnnotation(AppComponentsContainerConfig.class).order();
            int order2 =
                    o2.getAnnotation(AppComponentsContainerConfig.class).order();
            return order1 - order2;
        };
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        List<Method> annotatedMethods = getAnnotatedMethods(configClass);
        annotatedMethods.forEach(method -> putAppComponentToContext(method, configClass));
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    private List<Method> getAnnotatedMethods(Class<?> configClass) {
        List<Method> methods = new ArrayList<>();

        Arrays.stream(configClass.getMethods()).forEach(method -> {
            if (method.isAnnotationPresent(AppComponent.class)) methods.add(method);
        });

        return sortAnnotatedMethods(methods);
    }

    private List<Method> sortAnnotatedMethods(List<Method> methods) {
        var comparator = new Comparator<Method>() {
            @Override
            public int compare(Method method1, Method method2) {
                int o1 = getAppComponentAnnotation(method1).order();
                int o2 = getAppComponentAnnotation(method2).order();
                return o1 - o2;
            }
        };

        return methods.stream().sorted(comparator).toList();
    }

    private AppComponent getAppComponentAnnotation(Method method) {
        return method.getAnnotation(AppComponent.class);
    }

    private void putAppComponentToContext(Method method, Class<?> configClass) {
        String appComponentName = getAppComponentAnnotation(method).name().toLowerCase();
        Object component = getComponent(method, configClass);

        addToContext(component, appComponentName);
    }

    private void addToContext(Object component, String appComponentName) {
        appComponents.add(component);

        if (appComponentsByName.containsKey(appComponentName)) {
            throw new IllegalArgumentException(String.format("Duplicate app component name %s", appComponentName));
        }
        appComponentsByName.put(appComponentName, component);
    }

    private Object getComponent(Method method, Class<?> configClass) {
        try {
            Object[] args = Arrays.stream(method.getParameters())
                    .map(parameter -> parameter.getType().getSimpleName().toLowerCase())
                    .toArray();
            return invokeMethod(method, args, configClass);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Object invokeMethod(Method method, Object[] args, Class<?> configClass) throws Exception {
        var constructor = configClass.getConstructor();

        if (args.length == 0) {
            return method.invoke(constructor.newInstance());
        }

        var argsObjects = new ArrayList<>();
        for (var arg : args) {
            var obj = appComponentsByName.get(String.valueOf(arg));
            argsObjects.add(obj);
        }
        return method.invoke(constructor.newInstance(), argsObjects.toArray());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(Class<C> componentClass) {
        var cmt = appComponents.stream()
                .filter(component -> isContextComponent(componentClass, component))
                .toList();
        if (cmt.size() != 1) {
            throw new IllegalArgumentException(
                    String.format("Expected exactly one app component for %s", componentClass.getName()));
        }
        return (C) cmt.getFirst();
    }

    private <C> boolean isContextComponent(Class<C> componentClass, Object component) {
        if (componentClass.isInterface()) {
            return componentClass.isAssignableFrom(component.getClass());
        }
        return component.getClass().equals(componentClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <C> C getAppComponent(String componentName) {
        var cmt = (C) appComponentsByName.get(componentName.toLowerCase());

        if (cmt == null) {
            throw new IllegalArgumentException(String.format("App component %s not found", componentName));
        }

        return cmt;
    }
}
