package ru.otus.homework.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.homework.api.SensorDataProcessor;
import ru.otus.homework.api.model.SensorData;
import ru.otus.homework.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final SensorDataBufferedWriter writer;
    private final BlockingQueue<SensorData> buffer;
    private final Lock lock;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.writer = writer;
        buffer = new ArrayBlockingQueue<>(bufferSize);
        lock = new ReentrantLock();
    }

    @Override
    public void process(SensorData data) {
        boolean added;
        lock.lock();
        try {
            added = buffer.offer(data);
            if (!added) {
                log.warn("Буфер показаний переполнен, выполняется сброс данных");
                flush();
                added = buffer.offer(data);
                if (!added) {
                    log.error("Не удалось добавить данные даже после сброса буфера");
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void flush() {
        lock.lock();
        try {
            if (!buffer.isEmpty()) {
                var list = new ArrayList<>(buffer);
                list.sort(Comparator.comparing(SensorData::value));
                writer.writeBufferedData(list);
                buffer.clear();
            }
        } catch (Exception e) {
            log.error("Ошибка в процессе записи буфера", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
