package ru.otus.homework.listener.homework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import ru.otus.homework.listener.Listener;
import ru.otus.homework.model.Message;

public class HistoryListener implements Listener, HistoryReader {
    private final Map<Long, Message> history;

    public HistoryListener() {
        history = new HashMap<>();
    }

    @Override
    public void onUpdated(Message msg) {
        Message msgCopy = msg.clone();
        history.put(msg.getId(), msgCopy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return history.entrySet().stream()
                .filter(entry -> entry.getKey() == id)
                .findFirst()
                .map(Map.Entry::getValue);
    }
}
