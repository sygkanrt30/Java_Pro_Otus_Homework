package ru.otus.homework.sessionmanager;

public interface TransactionRunner {
    <T> T doInTransaction(TransactionAction<T> action);
}
