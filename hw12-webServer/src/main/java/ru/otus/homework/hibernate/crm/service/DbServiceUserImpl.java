package ru.otus.homework.hibernate.crm.service;

import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.hibernate.core.repository.DataTemplate;
import ru.otus.homework.hibernate.core.sessionmanager.TransactionManager;
import ru.otus.homework.hibernate.crm.model.User;

@AllArgsConstructor
@Slf4j
public class DbServiceUserImpl implements DBServiceUser {
    private final DataTemplate<User> userDataTemplate;
    private final TransactionManager transactionManager;

    @Override
    public Optional<User> findByLogin(String login) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var userList = userDataTemplate.findByEntityField(session, "name", login);
            var user = userList.stream().findFirst();
            log.info("user: {}", user);
            return user;
        });
    }
}
