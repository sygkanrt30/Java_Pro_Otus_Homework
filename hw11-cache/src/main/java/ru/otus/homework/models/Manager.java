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
public class Manager {
    @Id
    Long no;

    String label;

    String param1;

    public Manager(String label) {
        this.label = label;
    }
}
