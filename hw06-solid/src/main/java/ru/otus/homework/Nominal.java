package ru.otus.homework;

public record Nominal(int nominal) implements Comparable<Nominal> {
    @Override
    public int compareTo(Nominal nominal) {
        return Integer.compare(nominal.nominal, this.nominal);
    }
}
