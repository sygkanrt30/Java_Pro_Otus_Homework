package ru.otus.homework.processor.homework;

import ru.otus.homework.model.Message;
import ru.otus.homework.processor.Processor;

public class SwapProcessor implements Processor {
    @Override
    public Message process(Message message) {
        String field11 = message.getField11();
        String field12 = message.getField12();
        return message.toBuilder().field11(field12).field12(field11).build();
    }
}
