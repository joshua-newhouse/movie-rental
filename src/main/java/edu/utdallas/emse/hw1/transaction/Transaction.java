package edu.utdallas.emse.hw1.transaction;

import edu.utdallas.emse.hw1.Customer;
import edu.utdallas.emse.hw1.rental.Rentable;
import edu.utdallas.emse.hw1.serialization.Serialized;
import edu.utdallas.emse.hw1.transaction.frpstrategy.TransactionFRPStrategy;
import edu.utdallas.emse.hw1.transaction.frpstrategy.TransactionFRPStrategyFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Serialized(tag = "transaction")
public class Transaction {
    @Serialized(tag = "total-cost")
    private double totalCost;

    @Serialized(tag = "frequent-renter-points")
    private int frequentRenterPoints;

    @Serialized
    private List<Rentable> rentals = new ArrayList<>();

    @Serialized
    private List<Rentable> freeRentals = new ArrayList<>();

    private TransactionFRPStrategy tfrpStrategy;

    public Transaction(Customer c, List<Rentable> rentals) {
        this.rentals.addAll(
                rentals.stream()
                        .sorted(Comparator.comparing(Rentable::getPrice))
                        .collect(Collectors.toList()));

        /* Determine free rentals and deduct frequent renter points */
        int freeRentalsRedeemed = determineFreeRentals(c.getFrequentRenterPoints() / 10);
        c.deductFrequentRenterPoints(freeRentalsRedeemed * 10);

        /* Calculate this transactions cost and frequent rental points */
        this.tfrpStrategy = TransactionFRPStrategyFactory.getStrategy(c, rentals);
        totalCost = calcTotalCost();
        frequentRenterPoints = calcTotalFrequentRenterPoints();

        /* Apply this transaction to the customer */
        c.addToBalance(totalCost);
        c.addToFrequentRenterPoints(frequentRenterPoints);
    }

    public List<Rentable> getRentals() {
        return rentals;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\tTRANSACTION\n");
        sb.append("\t\tRentals:\n");
        rentals.forEach(rental -> sb.append("\t\t\t").append(rental.toString()).append("\n"));

        sb.append("\t\tFree Rentals:\n");
        freeRentals.forEach(freeRental -> sb.append("\t\t\t").append(freeRental.toString()).append("\n"));

        sb.append("\t\tTransaction price: ").append(totalCost)
                .append("\t\tTransaction Renter Points: ").append(frequentRenterPoints).append("\n");

        return sb.toString();
    }

    private double calcTotalCost() {
        return rentals.stream()
                .map(rental -> rental.getPrice())
                .reduce(0.0, (a, b) -> a + b);
    }

    private int calcTotalFrequentRenterPoints() {
        return tfrpStrategy.getFrequentRentalPoints(this);
    }

    private int determineFreeRentals(int freeRentals) {
        int i = 0;
        for (; i < freeRentals && i < rentals.size(); i++) {
            this.freeRentals.add(rentals.remove(i));
        }

        return i;
    }
}
