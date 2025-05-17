package ru.otus.homework.listener.homework;

import java.util.Optional;
import ru.otus.homework.model.Message;

public interface HistoryReader {
    Optional<Message> findMessageById(long id);
}
