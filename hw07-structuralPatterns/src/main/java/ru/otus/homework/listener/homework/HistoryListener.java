package ru.otus.homework.listener.homework;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;
import ru.otus.homework.listener.Listener;
import ru.otus.homework.model.Message;

public class HistoryListener implements Listener, HistoryReader {
    private final Queue<Message> history;

    public HistoryListener() {
        history = new LinkedList<>();
    }

    @Override
    public void onUpdated(Message msg) {
        Message msgCopy = msg.clone();
        history.add(msgCopy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return history.stream().filter(m -> m.getId() == id).findFirst();
    }
}
