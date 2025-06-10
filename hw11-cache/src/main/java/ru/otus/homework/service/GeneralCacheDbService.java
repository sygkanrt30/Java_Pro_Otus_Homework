package ru.otus.homework.service;

import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ru.otus.homework.cachehw.HwCache;
import ru.otus.homework.cachehw.HwListener;
import ru.otus.homework.cachehw.MyCache;
import ru.otus.homework.core.DataTemplate;
import ru.otus.homework.sessionmanager.TransactionRunner;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class GeneralCacheDbService<T> implements GeneralDbService<T> {
    DataTemplate<T> dataTemplate;
    TransactionRunner transactionRunner;
    HwCache<Long, T> cache;

    public GeneralCacheDbService(TransactionRunner transactionRunner, DataTemplate<T> dataTemplate) {
        this.dataTemplate = dataTemplate;
        this.transactionRunner = transactionRunner;
        this.cache = new MyCache<>();
        HwListener<Long, T> listener = new HwListener<>() {
            @Override
            public void notify(Long key, T value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        cache.addListener(listener);
    }

    @Override
    @SuppressWarnings("All")
    public T saveEntity(T entity) {
        return transactionRunner.doInTransaction(connection -> {
            try {
                var entityId = dataTemplate.insert(connection, entity);
                var entityFromDb = dataTemplate.findById(connection, entityId);
                var entityFromOptional = entityFromDb.get();
                log.info("created entity: {}", entityFromOptional);
                cache.put(entityId, entityFromOptional);
                return entityFromOptional;
            } catch (Exception e) {
                dataTemplate.update(connection, entity);
                log.info("updated entity: {}", entity);
                return entity;
            }
        });
    }

    @Override
    @SuppressWarnings("All")
    public Optional<T> getEntity(long id) {
        return transactionRunner.doInTransaction(connection -> {
            if (cache.get(id) != null) {
                var entityOptional = dataTemplate.findById(connection, id);
                log.info("entity: {}", entityOptional);
                cache.put(id, entityOptional.get());
                return entityOptional;
            }
            return Optional.of(cache.get(id));
        });
    }

    @Override
    public List<T> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var entityList = dataTemplate.findAll(connection);
            log.info("entityList:{}", entityList);
            return entityList;
        });
    }
}
