package ru.otus.homework.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;

import java.util.Set;


@Table(name = "client")
public record Client(
        @Id Long id,
        @NonNull String name,
        @MappedCollection(idColumn = "address_id") Address address,
        @MappedCollection(idColumn = "client_id") Set<Phone> phones) {}
