package ru.otus.homework;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.NonFinal;

@Data
public class Storage {
    @Setter(AccessLevel.NONE)
    @NonFinal
    int sumOfRemainder;

    Map<Nominal, Integer> nominalMap;

    public Storage() {
        nominalMap = new TreeMap<>(Collections.reverseOrder());
        sumOfRemainder = 0;
    }

    public void put(Nominal nominal) {
        increaseSumOfAllMoney(nominal);
        if (nominalExistsInMap(nominal)) {
            nominalMap.put(nominal, nominalMap.get(nominal) + 1);
            return;
        }
        nominalMap.putIfAbsent(nominal, 1);
    }

    private void increaseSumOfAllMoney(Nominal nominal) {
        sumOfRemainder += nominal.nominal();
    }

    private boolean nominalExistsInMap(Nominal nominal) {
        return nominalMap.containsKey(nominal);
    }

    public List<Integer> getMoney(int necessaryAmount) {
        var moneyForIssuing = new ArrayList<Integer>();
        AtomicInteger necAmount = new AtomicInteger(necessaryAmount);
        nominalMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> distributeByNominal(entry, necAmount, moneyForIssuing));
        checkIfIssuanceSuccessfully(moneyForIssuing, necAmount);
        updateReminder(necessaryAmount);
        return moneyForIssuing;
    }

    private void distributeByNominal(
            Map.Entry<Nominal, Integer> entry, AtomicInteger necAmount, ArrayList<Integer> moneyForIssuing) {
        if (isAllBanknotesOfThisNominalSuitable(entry, necAmount)) {
            addToListOfIssuance(entry.getValue(), entry, moneyForIssuing, necAmount);
        } else {
            addIfNotAllBanknotesSuitable(entry, necAmount, moneyForIssuing);
        }
    }

    private boolean isAllBanknotesOfThisNominalSuitable(Map.Entry<Nominal, Integer> entry, AtomicInteger necAmount) {
        return necAmount.get() - (entry.getValue() * entry.getKey().nominal()) >= 0;
    }

    private void addToListOfIssuance(
            int count, Map.Entry<Nominal, Integer> entry, ArrayList<Integer> moneyForIssuing, AtomicInteger necAmount) {
        addBanknoteInList(count, entry.getKey(), moneyForIssuing);
        necAmount.addAndGet(-(count * entry.getKey().nominal()));
    }

    private void addBanknoteInList(int countOfBanknote, Nominal nominal, ArrayList<Integer> moneyForIssuing) {
        for (int i = 0; i < countOfBanknote; i++) {
            moneyForIssuing.add(nominal.nominal());
        }
    }

    private void addIfNotAllBanknotesSuitable(
            Map.Entry<Nominal, Integer> entry, AtomicInteger necAmount, ArrayList<Integer> moneyForIssuing) {
        for (int i = entry.getValue(); i > 0; i--) {
            if ((necAmount.get() - (i * entry.getKey().nominal())) >= 0) {
                addToListOfIssuance(i, entry, moneyForIssuing, necAmount);
                break;
            }
        }
    }

    private void checkIfIssuanceSuccessfully(ArrayList<Integer> moneyForIssuing, AtomicInteger necAmount) {
        if (moneyForIssuing.isEmpty() || necAmount.get() != 0) {
            throw new RuntimeException("We cannot fully give out this amount");
        }
    }

    private void updateReminder(int necessaryAmount) {
        sumOfRemainder -= necessaryAmount;
    }

    public int getReminder() {
        return sumOfRemainder;
    }
}
