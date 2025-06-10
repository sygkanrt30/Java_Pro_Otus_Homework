package ru.otus.homework.service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.core.DataTemplate;
import ru.otus.homework.models.Client;
import ru.otus.homework.sessionmanager.TransactionRunner;

@Slf4j
public class DbServiceClientImpl implements DbServiceClient {
    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionRunner.doInTransaction(connection -> {
            var clientOptional = dataTemplate.findById(connection, id);
            log.info("client: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
