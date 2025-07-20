package ru.otus.homework.model.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.NonNull;


@Table(name = "phone")
public record Phone(
        @Id @Column("phone_id") Long id,
        @NonNull String number,
        @Column("client_id") Long clientId){}


