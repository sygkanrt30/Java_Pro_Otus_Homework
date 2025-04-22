package ru.otus.homework.proxy;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
public class TestLogging implements TestLoggingInterface {
    @Override
    public void calculation(int param1) {
        log.info("CALCULATION ONE PARAM");
    }

    @Override
    @Log
    public void calculation(int param1, int param2) {
        log.info("CALCULATION TWO PARAM");
    }

    @Override
    @Log
    public void calculation(int param1, int param2, String param3) {
        log.info("CALCULATION THREE PARAM");
    }
}
