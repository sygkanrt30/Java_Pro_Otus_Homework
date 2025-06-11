package ru.otus.homework.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class MyCache<K, V> implements HwCache<K, V> {
    Map<K, V> cacheStorage;
    List<HwListener<K, V>> listeners;

    public MyCache() {
        this.cacheStorage = new WeakHashMap<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        cacheStorage.put(key, value);
        notifyListeners(key, value, "put");
    }

    @Override
    public void remove(K key) {
        V value = cacheStorage.remove(key);
        notifyListeners(key, value, "remove");
    }

    @Override
    public V get(K key) {
        if (!cacheStorage.containsKey(key)) return null;
        V result = cacheStorage.get(key);
        notifyListeners(key, result, "get");
        return result;
    }

    private void notifyListeners(K key, V result, String action) {
        try {
            listeners.forEach(listener -> listener.notify(key, result, action));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
    }
}
