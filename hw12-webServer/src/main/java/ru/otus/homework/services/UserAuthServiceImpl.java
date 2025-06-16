package ru.otus.homework.services;

import lombok.AllArgsConstructor;
import ru.otus.homework.hibernate.crm.service.DBServiceUser;

@AllArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {
    private final DBServiceUser userService;

    @Override
    public boolean authenticate(String login, String password) {
        return userService
                .findByLogin(login)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}
