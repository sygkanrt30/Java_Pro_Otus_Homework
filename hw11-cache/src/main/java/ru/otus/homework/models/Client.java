package ru.otus.homework.models;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.otus.homework.mapper.Id;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client {
    @Id
    Long id;

    String name;

    public Client(String name) {
        this.id = null;
        this.name = name;
    }
}
