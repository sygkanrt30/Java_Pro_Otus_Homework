package ru.otus.homework;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.Map;
import lombok.experimental.NonFinal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AtmTest {
    @NonFinal
    BanknoteDistributionSystem banknoteDistributionSystem;

    @NonFinal
    BanknoteIssuanceSystem banknoteIssuanceSystem;

    @BeforeEach
    void setUp() {
        var storage = new Storage();
        banknoteIssuanceSystem = new BanknoteIssuanceSystem(storage);
        banknoteDistributionSystem = new BanknoteDistributionSystem(storage);
    }

    @Test
    void fillInBanknoteTest() {
        int[] banknote = {10, 100, 500, 10, 5000, 500, 10, 10, 10};
        var atm = new Atm(banknoteDistributionSystem, banknoteIssuanceSystem);
        var nominal10 = new Nominal(10);
        var nominal50 = new Nominal(50);
        var nominal100 = new Nominal(100);
        var nominal500 = new Nominal(500);
        var nominal1000 = new Nominal(1000);
        var nominal5000 = new Nominal(5000);

        atm.fillInBanknote(banknote);

        Map<Nominal, Integer> map = atm.banknoteDistributionSystem().storage().nominalMap();
        assertThat(map.get(nominal10)).isEqualTo(5);
        assertThat(map.get(nominal50)).isEqualTo(null);
        assertThat(map.get(nominal100)).isEqualTo(1);
        assertThat(map.get(nominal500)).isEqualTo(2);
        assertThat(map.get(nominal1000)).isEqualTo(null);
        assertThat(map.get(nominal5000)).isEqualTo(1);
    }

    @Test
    void getMonetNotCorrectnessAmountTest1() {
        var atm = new Atm(banknoteDistributionSystem, banknoteIssuanceSystem);

        Assertions.assertThrows(IllegalArgumentException.class, () -> atm.getMoney(-3));
    }

    @Test
    void getMonetNotCorrectnessAmountTest2() {
        var atm = new Atm(banknoteDistributionSystem, banknoteIssuanceSystem);

        Assertions.assertThrows(IllegalArgumentException.class, () -> atm.getMoney(45));
    }

    @Test
    void getMoneyTest1() {
        int[] banknote = {10, 100, 500, 10, 5000, 500, 10, 10, 10};
        var atm = new Atm(banknoteDistributionSystem, banknoteIssuanceSystem);
        atm.fillInBanknote(banknote);

        List<Integer> money = atm.getMoney(5000);

        Assertions.assertEquals(1, money.size());
        Assertions.assertEquals(5000, money.getFirst());
    }

    @Test
    void getMoneyTest2() {
        int[] banknote = {10, 100, 500, 10, 5000, 500, 10, 10, 10};
        var atm = new Atm(banknoteDistributionSystem, banknoteIssuanceSystem);
        atm.fillInBanknote(banknote);

        Assertions.assertThrows(RuntimeException.class, () -> atm.getMoney(4500));
    }

    @Test
    void getMoneyTest3() {
        int[] banknote = {10, 100, 500, 10, 5000, 500, 10, 10, 10};
        var atm = new Atm(banknoteDistributionSystem, banknoteIssuanceSystem);
        atm.fillInBanknote(banknote);

        List<Integer> money = atm.getMoney(1110);

        Assertions.assertEquals(List.of(500, 500, 100, 10), money);
    }

    @Test
    void getReminder() {
        int[] banknote = {10, 100, 500, 10, 5000, 500, 10, 10, 10};
        var atm = new Atm(banknoteDistributionSystem, banknoteIssuanceSystem);
        atm.fillInBanknote(banknote);
        List<Integer> money = atm.getMoney(1110);

        int reminder = atm.getAtmBalance();

        Assertions.assertEquals(5040, reminder);
    }
}
