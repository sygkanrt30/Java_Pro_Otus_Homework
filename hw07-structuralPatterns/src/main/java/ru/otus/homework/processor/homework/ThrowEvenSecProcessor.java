package ru.otus.homework.processor.homework;

import lombok.AllArgsConstructor;
import ru.otus.homework.model.Message;
import ru.otus.homework.processor.Processor;

@AllArgsConstructor
public class ThrowEvenSecProcessor implements Processor {
    TimeProvider timeProvider;

    @Override
    public Message process(Message message) {
        var now = timeProvider.getTime();
        if (now.getSecond() % 2 == 0) {
            throw new RuntimeException("Это было исключение на четную минуту");
        }
        return message;
    }
}
