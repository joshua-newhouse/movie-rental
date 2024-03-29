package edu.utdallas.emse.hw1.transaction.frpstrategy;

import edu.utdallas.emse.hw1.rental.Rental;
import edu.utdallas.emse.hw1.transaction.Transaction;

public class AgeFRPStrategy implements TransactionFRPStrategy {
    private static final AgeFRPStrategy INSTANCE;

    static {
        INSTANCE = new AgeFRPStrategy();
    }

    private AgeFRPStrategy() {
    }

    public static AgeFRPStrategy getInstance() {
        return INSTANCE;
    }

    @Override
    public int getFrequentRentalPoints(Transaction t) {
        return 2 *
                t.getItems().stream()
                        .filter(item -> item instanceof Rental)
                        .map(rental -> ((Rental) rental).getFrequentRenterPoints())
                        .reduce(0, (a, b) -> a + b);
    }
}
