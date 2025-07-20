package ru.otus.homework.model.dto;

import javax.validation.constraints.NotBlank;

public record ClientDto(
        @NotBlank String name,
        @NotBlank String street,
        @NotBlank String phone) {}
