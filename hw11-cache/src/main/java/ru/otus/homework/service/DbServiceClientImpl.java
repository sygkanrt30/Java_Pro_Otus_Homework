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
    private final HwCache<String, Client> cache;

    public DbServiceClientImpl(
            TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate, HwCache<String, Client> cache) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;
        this.cache = cache;
        @SuppressWarnings("All")
        var listener = new HwListener<String, Client>() {
            @Override
            public void notify(String key, Client value, String action) {
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
                cache.put(String.valueOf(clientId), createdClient);
                return createdClient;
            }
            dataTemplate.update(connection, client);
            log.info("updated client: {}", client);
            cache.remove(String.valueOf(client.getId()));
            cache.put(String.valueOf(client.getId()), client);
            return client;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return transactionRunner.doInTransaction(connection -> {
            Client client = cache.get(String.valueOf(id));
            if (client == null) {
                var clientOptional = dataTemplate.findById(connection, id);
                var clientFound = clientOptional.orElseThrow(() -> new RuntimeException("Client not found"));
                log.info("client: {}", clientFound);
                cache.put(String.valueOf(id), clientFound);
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
            clientList.forEach(client -> cache.put(String.valueOf(client.getId()), client));
            return clientList;
        });
    }
}
