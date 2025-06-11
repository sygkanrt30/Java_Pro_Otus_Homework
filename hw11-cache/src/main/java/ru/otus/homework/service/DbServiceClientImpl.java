package ru.otus.homework.service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.cachehw.HwCache;
import ru.otus.homework.cachehw.HwListener;
import ru.otus.homework.core.DataTemplate;
import ru.otus.homework.models.Client;
import ru.otus.homework.sessionmanager.TransactionRunner;

@Slf4j
public class DbServiceClientImpl implements DbServiceClient {
    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;
    HwCache<Long, Client> cache;

    public DbServiceClientImpl(
            TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, HwCache<Long, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = cache;
        @SuppressWarnings("All")
        var listener = new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        cache.addListener(listener);
    }

    @Override
    public Client saveClient(Client client) {
        return transactionRunner.doInTransaction(connection -> {
            if (client.getId() == null) {
                var clientId = dataTemplate.insert(connection, client);
                var createdClient = new Client(clientId, client.getName());
                log.info("created client: {}", createdClient);
                cache.put(clientId, createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            cache.remove(client.getId());
            cache.put(client.getId(), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionRunner.doInTransaction(connection -> {
            Client client = cache.get(id);
            if (client == null) {
                var clientOptional = dataTemplate.findById(connection, id);
                log.info("client: {}", clientOptional);
                return clientOptional;
            }
            return Optional.of(client);
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
