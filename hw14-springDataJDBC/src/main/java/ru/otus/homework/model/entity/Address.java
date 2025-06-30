package ru.otus.homework.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;


@Table(name = "address")
public record Address(
        @Id @Column("address_id") Long id,
        @NonNull String street
) {}
