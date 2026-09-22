import java.util.UUID;

public class Program {
    public static void main(String[] args) {
        User john = new User("John", 1000);
        User anna = new User("Anna", 500);

        TransactionsList transactions = new TransactionsLinkedList();

        // 1. Empty list
        System.out.println("Empty list size: "
                + transactions.ToArray().length); // 0

        Transaction first = new Transaction(
                UUID.randomUUID(), anna, john,
                Transaction.category.DEBIT, -100);

        Transaction middle = new Transaction(
                UUID.randomUUID(), john, anna,
                Transaction.category.CREDIT, 50);

        Transaction last = new Transaction(
                UUID.randomUUID(), anna, john,
                Transaction.category.DEBIT, -25);

        // 2. Add transactions and check insertion order
        transactions.add(first);
        transactions.add(middle);
        transactions.add(last);

        System.out.println("\nAll transactions (expected: 3):");
        for (Transaction transaction : transactions.ToArray()) {
            System.out.println(transaction);
        }

        // 3. Remove the middle node
        transactions.removeById(middle.getId());

        System.out.println("\nAfter removing middle (expected: first, last):");
        for (Transaction transaction : transactions.ToArray()) {
            System.out.println(transaction);
        }

        // 4. Remove the tail, then append again
        transactions.removeById(last.getId());
        transactions.add(middle);

        System.out.println("\nAfter removing tail and adding (expected: first, middle):");
        for (Transaction transaction : transactions.ToArray()) {
            System.out.println(transaction);
        }

        // 5. Remove the head
        transactions.removeById(first.getId());

        System.out.println("\nAfter removing head (expected: middle):");
        for (Transaction transaction : transactions.ToArray()) {
            System.out.println(transaction);
        }

        // 6. Try removing an already removed transaction
        System.out.println("\nRemoving a missing ID:");
        try {
            transactions.removeById(first.getId());
            System.out.println("FAIL: expected an exception");
        } catch (UserNotFoundException e) {
            System.out.println("Expected exception: " + e.getMessage());
        }

        // 7. Remove the only remaining node
        transactions.removeById(middle.getId());
        System.out.println("\nAfter removing everything: "
                + transactions.ToArray().length); // 0

        // 8. Try removing from an empty list
        System.out.println("\nRemoving from an empty list:");
        try {
            transactions.removeById(last.getId());
            System.out.println("FAIL: expected an exception");
        } catch (UserNotFoundException e) {
            System.out.println("Expected exception: " + e.getMessage());
        }

        // 9. Reuse the list after emptying it
        transactions.add(last);

        System.out.println("\nReused list (expected: last):");
        for (Transaction transaction : transactions.ToArray()) {
            System.out.println(transaction);
        }
    }
}