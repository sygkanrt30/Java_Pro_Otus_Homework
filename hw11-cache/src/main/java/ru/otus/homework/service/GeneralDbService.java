package ru.otus.homework.service;

import java.util.List;
import java.util.Optional;

public interface GeneralDbService<T> {
    T saveEntity(T entity);

    Optional<T> getEntity(long id);

    List<T> findAll();
}
