package ru.otus.homework;

import java.util.List;

public record BanknoteIssuanceSystem(Storage storage) {
    public List<Integer> getMoney(int necessaryAmount) {
        checkAmountByCorrectness(necessaryAmount);
        return storage.getMoney(necessaryAmount);
    }

    private void checkAmountByCorrectness(int necessaryAmount) {
        if (necessaryAmount <= 0) {
            throw new IllegalArgumentException("necessaryAmount must be greater than 0");
        }
        if (necessaryAmount % 10 != 0) {
            throw new IllegalArgumentException("necessaryAmount must be divisible by 10");
        }
    }

    public int getSumOfReminder() {
        return storage.getReminder();
    }
}
