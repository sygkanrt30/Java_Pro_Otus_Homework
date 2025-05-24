package ru.otus.homework.model;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ObjectForMessage {
    List<String> data;

    private ObjectForMessage(List<String> data) {
        this.data = data;
    }

    public ObjectForMessage copy() {
        if (data != null) {
            return new ObjectForMessage(List.copyOf(data));
        }
        return new ObjectForMessage(null);
    }
}
