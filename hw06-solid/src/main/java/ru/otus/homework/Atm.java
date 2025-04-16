package ru.otus.homework;

import java.util.List;

public record Atm(
        BanknoteDistributionSystem banknoteDistributionSystem, BanknoteIssuanceSystem banknoteIssuanceSystem) {
    public void fillInBanknote(int... banknote) {
        banknoteDistributionSystem.addBanknotes(banknote);
    }

    public List<Integer> getMoney(int necessaryAmount) {
        return banknoteIssuanceSystem.getMoney(necessaryAmount);
    }

    public int getAtmBalance() {
        return banknoteIssuanceSystem.getSumOfReminder();
    }
}
