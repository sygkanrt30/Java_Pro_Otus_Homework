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
    HwCache<Long, Manager> cache;

    public DbServiceManagerImpl(
            TransactionRunner transactionRunner,
            DataTemplate<Manager> managerDataTemplate,
            HwCache<Long, Manager> cache) {
        this.transactionRunner = transactionRunner;
        this.managerDataTemplate = managerDataTemplate;
        this.cache = cache;
        @SuppressWarnings("All")
        var listener = new HwListener<Long, Manager>() {
            @Override
            public void notify(Long key, Manager value, String action) {
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
                cache.put(managerNo, createdManager);
                return createdManager;
            }
            managerDataTemplate.update(connection, manager);
            log.info("updated manager: {}", manager);
            cache.remove(manager.getNo());
            cache.put(manager.getNo(), manager);
            return manager;
        });
    }

    @Override
    public Optional<Manager> getManager(long no) {
        return transactionRunner.doInTransaction(connection -> {
            Manager manager = cache.get(no);
            if (manager == null) {
                var managerOptional = managerDataTemplate.findById(connection, no);
                log.info("manager: {}", managerOptional);
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
            return managerList;
        });
    }
}
