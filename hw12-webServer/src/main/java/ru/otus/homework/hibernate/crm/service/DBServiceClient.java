package ru.otus.homework.hibernate.crm.service;

import java.util.List;
import java.util.Optional;
import ru.otus.homework.hibernate.crm.model.Client;

public interface DBServiceClient {

    Client saveClient(Client client);

    Optional<Client> getClient(long id);

    List<Client> findAll();
}
