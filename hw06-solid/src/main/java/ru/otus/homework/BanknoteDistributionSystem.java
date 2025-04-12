package ru.otus.homework;

public record BanknoteDistributionSystem(Storage storage) {
    public void addBanknotes(int[] banknote) {
        for (int j : banknote) {
            var nominal = new Nominal(j);
            putBanknoteInStorage(nominal);
        }
    }

    private void putBanknoteInStorage(Nominal nominal) {
        storage.put(nominal);
    }
}
