import java.util.NoSuchElementException;
import java.util.Scanner;

public class Menu {
    private final UsersList usersList;
    private final TransactionsService service;
    private final boolean devMode;
    private final Scanner scanner;

    public Menu(boolean devMode) {
        usersList = new UsersArrayList();
        service = new TransactionsService(usersList);
        this.devMode = devMode;
        scanner = new Scanner(System.in);
    }

    public void run() {
        while (true) {
            printOptions();
            try {
                int choice = Integer.parseInt(readLine());
                if (choice == (devMode ? 7 : 5)) {
                    return;
                }
                switch (choice) {
                    case 1:
                        addUser();
                        break;
                    case 2:
                        viewBalance();
                        break;
                    case 3:
                        performTransfer();
                        break;
                    case 4:
                        viewTransactions();
                        break;
                    case 5:
                        if (devMode) {
                            removeTransferById();
                        } else {
                            System.out.println("Unknown command");
                        }
                        break;
                    case 6:
                        if (devMode) {
                            checkValidity();
                        } else {
                            System.out.println("Unknown command");
                        }
                        break;
                    default:
                        System.out.println("Unknown command");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: please enter valid whole numbers within range");
            } catch (IllegalArgumentException | UserNotFoundException | IllegalTransactionException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NoSuchElementException e) {
                // End of input, including when input ends during an action.
                return;
            }
            System.out.println("---------------------------------------------------------");
        }
    }

    private void printOptions() {
        System.out.println("1. Add a user");
        System.out.println("2. View user balances");
        System.out.println("3. Perform a transfer");
        System.out.println("4. View all transactions for a specific user");
        if (devMode) {
            System.out.println("5. DEV - remove a transfer by ID");
            System.out.println("6. DEV - check transfer validity");
            System.out.println("7. Finish execution");
        } else {
            System.out.println("5. Finish execution");
        }
    }

    private String readLine() {
        System.out.print("-> ");
        return scanner.nextLine().trim();
    }

    private String[] readFields(int count) {
        String[] fields = readLine().split("\\s+");
        if (fields.length != count || fields[0].isEmpty()) {
            throw new IllegalArgumentException("Expected " + count + " values separated by spaces");
        }
        return fields;
    }

    private void addUser() {
        System.out.println("Enter a user name and a balance");
        String[] fields = readFields(2);
        User user = new User(fields[0], Long.parseLong(fields[1]));
        service.addUser(user);
        System.out.println("User with id = " + user.getID() + " is added");
    }

    private void viewBalance() {
        System.out.println("Enter a user ID");
        int id = Integer.parseInt(readLine());
        User user = usersList.RetrieveUserById(id);
        System.out.println(user.getName() + " - " + service.getBalance(id));
    }

    private void performTransfer() {
        System.out.println("Enter a sender ID, a recipient ID, and a transfer amount");
        String[] fields = readFields(3);
        int senderId = Integer.parseInt(fields[0]);
        int recipientId = Integer.parseInt(fields[1]);
        long amount = Long.parseLong(fields[2]);
        // The service accepts double: prevent rounding of large whole amounts.
        if (amount > 9007199254740992L) {
            throw new IllegalArgumentException("Transfer amount exceeds the supported exact range");
        }
        service.transfer(senderId, recipientId, amount);
        System.out.println("The transfer is completed");
    }

    private void viewTransactions() {
        System.out.println("Enter a user ID");
        int id = Integer.parseInt(readLine());
        Transaction[] transactions = service.getTransactionsFor(id);
        if (transactions.length == 0) {
            System.out.println("No transactions");
        }
        for (Transaction transaction : transactions) {
            boolean outgoing = isOutgoing(transaction);
            User other = outgoing ? transaction.getRecepient() : transaction.getSender();
            System.out.println((outgoing ? "To " : "From ") + describe(other)
                    + " " + (long) transaction.getAmount() + " with id = " + transaction.getId());
        }
    }

    private void removeTransferById() {
        System.out.println("Enter a user ID and a transfer ID");
        String[] fields = readFields(2);
        int userId = Integer.parseInt(fields[0]);
        Transaction target = null;
        for (Transaction transaction : service.getTransactionsFor(userId)) {
            if (transaction.getId().equals(fields[1])) {
                target = transaction;
                break;
            }
        }
        if (target == null) {
            throw new UserNotFoundException("Transaction with id = " + fields[1] + " not found");
        }
        service.removeTransaction(userId, fields[1]);
        boolean outgoing = isOutgoing(target);
        User other = outgoing ? target.getRecepient() : target.getSender();
        System.out.println("Transfer " + (outgoing ? "To " : "From ") + describe(other)
                + " " + (long) Math.abs(target.getAmount()) + " removed");
    }

    private void checkValidity() {
        System.out.println("Check results:");
        Transaction[] unpaired = service.checkValidity();
        if (unpaired.length == 0) {
            System.out.println("All transfers are valid");
        }
        for (Transaction transaction : unpaired) {
            boolean outgoing = isOutgoing(transaction);
            User owner = outgoing ? transaction.getSender() : transaction.getRecepient();
            User other = outgoing ? transaction.getRecepient() : transaction.getSender();
            System.out.println(describe(owner) + " has an unacknowledged transfer id = "
                    + transaction.getId() + (outgoing ? " to " : " from ") + describe(other)
                    + " for " + (long) Math.abs(transaction.getAmount()));
        }
    }

    private boolean isOutgoing(Transaction transaction) {
        return transaction.getTRansferCategory() == Transaction.category.DEBIT;
    }

    private String describe(User user) {
        return user.getName() + "(id = " + user.getID() + ")";
    }
}
