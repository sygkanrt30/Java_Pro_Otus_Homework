package ru.otus.homework.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.SneakyThrows;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final Constructor<T> constructor;
    private final String className;
    private final List<Field> fields;
    private final Field idField;

    @SneakyThrows
    public EntityClassMetaDataImpl(Class<T> entityClass) {
        this.constructor = entityClass.getConstructor();
        this.className = entityClass.getSimpleName();
        this.fields = Arrays.stream(entityClass.getDeclaredFields()).toList();
        this.idField = getFieldWithId();
    }

    private Field getFieldWithId() {
        return fields.stream()
                .filter(f -> f.isAnnotationPresent(Id.class))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Class " + className + " has no primary key field"));
    }

    @Override
    public String getName() {
        return className;
    }

    @Override
    @SneakyThrows
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return fields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return new ArrayList<>(fields)
                .stream().filter(field -> !field.equals(idField)).toList();
    }
}
