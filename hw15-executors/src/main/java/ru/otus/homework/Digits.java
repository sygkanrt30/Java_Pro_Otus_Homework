package ru.otus.homework;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Digits {
    private int currentNumber = 1;
    private boolean ascending = true;
    private boolean firstThreadPrinted = false;

    public static void main(String[] args) {
        var digits = new Digits();
        new Thread(() -> digits.action(0)).start();
        new Thread(() -> digits.action(1)).start();
    }

    private synchronized void action(int threadId) {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while ((threadId == 0 && firstThreadPrinted) ||
                        (threadId == 1 && !firstThreadPrinted)) {
                    this.wait();
                }
                log.info("{}", currentNumber);
                if (threadId == 0) {
                    firstThreadPrinted = true;
                } else {
                    if (ascending) {
                        if (currentNumber < 10) {
                            currentNumber++;
                        } else {
                            ascending = false;
                            currentNumber--;
                        }
                    } else {
                        if (currentNumber > 1) {
                            currentNumber--;
                        } else {
                            ascending = true;
                            currentNumber++;
                        }
                    }
                    firstThreadPrinted = false;
                }
                notifyAll();
                sleep();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
