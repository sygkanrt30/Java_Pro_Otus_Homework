package ru.otus.homework;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class CustomerService {
    private final TreeMap<Customer, String> customers;

    public CustomerService() {
        this.customers = new TreeMap<>(Comparator.comparing(Customer::getScores));
    }

    public Map.Entry<Customer, String> getSmallest() {
        Map.Entry<Customer, String> entry = customers.firstEntry();
        return getEntryWithCopyCustomer(entry);
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        Map.Entry<Customer, String> entry = customers.higherEntry(customer);
        return getEntryWithCopyCustomer(entry);
    }

    private AbstractMap.SimpleEntry<Customer, String> getEntryWithCopyCustomer(Map.Entry<Customer, String> entry) {
        if (entry != null) {
            Customer customerCopy = entry.getKey().copy();
            return new AbstractMap.SimpleEntry<>(customerCopy, entry.getValue());
        }
        return null;
    }

    public void add(Customer customer, String data) {
        customers.put(customer, data);
    }
}
