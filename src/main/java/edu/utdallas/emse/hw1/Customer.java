package edu.utdallas.emse.hw1;

import edu.utdallas.emse.hw1.serialization.ObjectSerializable;
import edu.utdallas.emse.hw1.serialization.Serialized;
import edu.utdallas.emse.hw1.transaction.Transaction;

import java.util.List;
import java.util.ArrayList;

@Serialized
public class Customer implements ObjectSerializable {
    @Serialized
    private String name;

    @Serialized
    private int age;

    @Serialized
    private List<Transaction> transactions = new ArrayList();

    @Serialized(tag="amount-owed")
    private double totalBalance = 0.0;

    @Serialized(tag="frequent-renter-points")
    private int frequentRenterPoints = 0;

    public Customer(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
        totalBalance += t.getPrice();
        frequentRenterPoints += t.getFrequentRenterPoints();
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getTotalAmountOwed() {
        return totalBalance;
    }

    public int getFrequentRenterPoints() {
        return frequentRenterPoints;
    }

    public String getStatement() {
        StringBuilder statement = new StringBuilder("Rental Record for " + getName() + "\n");

        /* show figures for the transactions */
        transactions.forEach(transaction -> statement.append(transaction.toString()));

        /* add footer lines */
        statement.append("Amount owed is ").append(getTotalAmountOwed()).append("\n")
                .append("You earned ").append(getFrequentRenterPoints()).append(" frequent renter points");

        return statement.toString();
    }
}