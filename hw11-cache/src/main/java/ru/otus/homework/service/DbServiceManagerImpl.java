package ru.otus.homework.service;

import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.cachehw.HwCache;
import ru.otus.homework.cachehw.HwListener;
import ru.otus.homework.core.DataTemplate;
import ru.otus.homework.models.Manager;
import ru.otus.homework.sessionmanager.TransactionRunner;

@Slf4j
public class DbServiceManagerImpl implements DbServiceManager {
    private final DataTemplate<Manager> managerDataTemplate;
    private final TransactionRunner transactionRunner;
    private final HwCache<String, Manager> cache;

    public DbServiceManagerImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Manager> managerDataTemplate,
            HwCache<String, Manager> cache) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.cache = cache;
        @SuppressWarnings("All")
        var listener = new HwListener<String, Manager>() {
            @Override
            public void notify(String key, Manager value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        cache.addListener(listener);
    }

    @Override
    public Manager saveManager(Manager manager) {
        return transactionRunner.doInTransaction(connection -> {
            if (manager.getNo() == null) {
                var managerNo = managerDataTemplate.insert(connection, manager);
                var createdManager = new Manager(managerNo, manager.getLabel(), manager.getParam1());
                log.info("created manager: {}", createdManager);
                cache.put(String.valueOf(managerNo), createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            cache.remove(String.valueOf(manager.getNo()));
            cache.put(String.valueOf(manager.getNo()), manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        return transactionRunner.doInTransaction(connection -> {
            Manager manager = cache.get(String.valueOf(no));
            if (manager == null) {
                var managerOptional = managerDataTemplate.findById(connection, no);
                var managerFound = managerOptional.orElseThrow(() -> new RuntimeException("no manager found"));
                log.info("manager: {}", managerFound);
                cache.put(String.valueOf(managerFound.getNo()), managerFound);
                return managerOptional;
            }
            return Optional.of(manager);
        });
    }

    @Override
    public List<Manager> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var managerList = managerDataTemplate.findAll(connection);
            log.info("managerList:{}", managerList);
            managerList.forEach(manager -> cache.put(String.valueOf(manager.getNo()), manager));
            return managerList;
        });
    }
}
