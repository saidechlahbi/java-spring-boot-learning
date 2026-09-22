public class Program {
    public static void main(String[] args) {
        TransactionsService service = new TransactionsService();
        User john = new User("John", 1000);
        User anna = new User("Anna", 500);
        service.addUser(john);
        service.addUser(anna);

        check(service.getTransactionsFor(john.getID()).length == 0, "Empty history");
        check(service.checkValidity().length == 0, "Empty histories are valid");

        service.transfer(john.getID(), anna.getID(), 200);
        service.transfer(anna.getID(), john.getID(), 50);
        check(service.getBalance(john.getID()) == 850, "John balance is 850");
        check(service.getBalance(anna.getID()) == 650, "Anna balance is 650");
        check(service.getTransactionsFor(john.getID()).length == 2, "John has two records");
        check(service.getTransactionsFor(anna.getID()).length == 2, "Anna has two records");
        check(service.checkValidity().length == 0, "All transactions are paired");

        System.out.println("\nJohn's transactions:");
        for (Transaction transaction : service.getTransactionsFor(john.getID())) {
            System.out.println(transaction);
        }
        System.out.println("\nAnna's transactions:");
        for (Transaction transaction : service.getTransactionsFor(anna.getID())) {
            System.out.println(transaction);
        }

        System.out.println("\nRejected transfers:");
        double[] invalidAmounts = {2000, 0, -10, 1.5, Double.NaN, Double.POSITIVE_INFINITY};
        for (double amount : invalidAmounts) {
            try {
                service.transfer(john.getID(), anna.getID(), amount);
                throw new AssertionError("Accepted invalid amount: " + amount);
            } catch (IllegalTransactionException e) {
                System.out.println(amount + ": " + e.getMessage());
            }
        }
        try {
            service.transfer(john.getID(), john.getID(), 10);
            throw new AssertionError("Accepted a self-transfer");
        } catch (IllegalTransactionException e) {
            System.out.println("Self-transfer: " + e.getMessage());
        }
        try {
            service.getBalance(-1);
            throw new AssertionError("Accepted a missing user");
        } catch (UserNotFoundException e) {
            System.out.println("Missing user: " + e.getMessage());
        }
        check(service.getBalance(john.getID()) == 850
                && service.getBalance(anna.getID()) == 650,
                "Rejected transfers leave balances unchanged");
        check(service.getTransactionsFor(john.getID()).length == 2
                && service.getTransactionsFor(anna.getID()).length == 2,
                "Rejected transfers leave histories unchanged");

        String id = service.getTransactionsFor(john.getID())[0].getId();
        service.removeTransaction(john.getID(), id);
        Transaction[] unpaired = service.checkValidity();
        check(unpaired.length == 1 && unpaired[0].getId().equals(id)
                && unpaired[0].getTRansferCategory() == Transaction.category.CREDIT,
                "Deleting John's debit leaves Anna's credit unpaired");
        System.out.println("\nUnpaired transaction: " + unpaired[0]);

        try {
            service.removeTransaction(john.getID(), id);
            throw new AssertionError("Removed a missing transaction");
        } catch (UserNotFoundException e) {
            System.out.println("Missing transaction: " + e.getMessage());
        }
        service.removeTransaction(anna.getID(), id);
        check(service.checkValidity().length == 0, "Removing both sides restores validity");
        check(service.getBalance(john.getID()) == 850
                && service.getBalance(anna.getID()) == 650,
                "Deleting history does not change balances");
        System.out.println("\nAll checks passed.");
    }

    private static void check(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
        System.out.println("PASS: " + message);
    }
}
