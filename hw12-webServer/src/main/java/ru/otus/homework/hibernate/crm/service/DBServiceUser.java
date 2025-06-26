package ru.otus.homework.hibernate.crm.service;

import java.util.Optional;
import ru.otus.homework.hibernate.crm.model.User;

public interface DBServiceUser {
    Optional<User> findByLogin(String login);
}
