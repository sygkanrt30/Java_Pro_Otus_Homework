package ru.otus.homework;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import ru.otus.homework.handler.ComplexProcessor;
import ru.otus.homework.listener.ListenerPrinterConsole;
import ru.otus.homework.listener.homework.HistoryListener;
import ru.otus.homework.model.Message;
import ru.otus.homework.model.ObjectForMessage;
import ru.otus.homework.processor.LoggerProcessor;
import ru.otus.homework.processor.homework.SwapProcessor;
import ru.otus.homework.processor.homework.ThrowEvenSecProcessor;

public class HomeWork {

    public static void main(String[] args) {
        var processors = List.of(new LoggerProcessor(new ThrowEvenSecProcessor(LocalTime::now)), new SwapProcessor());

        var complexProcessor = new ComplexProcessor(processors, ex -> {});
        var listenerPrinter = new ListenerPrinterConsole();
        var historyListener = new HistoryListener();
        complexProcessor.addListener(listenerPrinter);
        complexProcessor.addListener(historyListener);
        ObjectForMessage field13 = new ObjectForMessage();
        ArrayList<String> data = new ArrayList<>();
        data.add("123");
        data.add("456");
        field13.setData(data);

        var message = new Message.Builder(1L)
                .field11("field11")
                .field12("field12")
                .field13(field13)
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);

        complexProcessor.removeListener(listenerPrinter);
        complexProcessor.removeListener(historyListener);
    }
}
