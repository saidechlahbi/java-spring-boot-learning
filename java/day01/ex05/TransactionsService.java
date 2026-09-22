import java.util.UUID;

public class TransactionsService {
    private final UsersList usersList;
    private History histories;

    // User has no transaction field yet, so the service owns its histories.
    private static class History {
        final int userId;
        final TransactionsList transactions = new TransactionsLinkedList();
        History next;

        History(int userId, History next) {
            this.userId = userId;
            this.next = next;
        }
    }

    public TransactionsService() {
        this(new UsersArrayList());
    }

    public TransactionsService(UsersList usersList) {
        if (usersList == null) {
            throw new IllegalArgumentException("Users list cannot be null");
        }
        this.usersList = usersList;
    }

    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        for (int i = 0; i < usersList.RetrieveTheNumberOfUsers(); i++) {
            if (usersList.RetrieveUserbyIndex(i).getID() == user.getID()) {
                throw new IllegalArgumentException("User is already registered");
            }
        }
        usersList.AddUser(user);
    }

    public long getBalance(int userId) {
        return usersList.RetrieveUserById(userId).getBalance();
    }

    public void transfer(int senderId, int recipientId, double amount) {
        User sender = usersList.RetrieveUserById(senderId);
        User recipient = usersList.RetrieveUserById(recipientId);

        if (senderId == recipientId) {
            throw new IllegalTransactionException("Cannot transfer to the same user");
        }
        // Balances are longs: reject fractions instead of truncating them.
        if (!Double.isFinite(amount) || amount <= 0
                || amount != Math.floor(amount) || amount >= 0x1.0p63) {
            throw new IllegalTransactionException("Amount must be a positive whole number within long range");
        }
        long value = (long) amount;
        if (value > sender.getBalance()) {
            throw new IllegalTransactionException("Sender has insufficient funds");
        }
        if (recipient.getBalance() > Long.MAX_VALUE - value) {
            throw new IllegalTransactionException("Recipient balance would overflow");
        }

        UUID id = UUID.randomUUID();
        Transaction debit = new Transaction(id, recipient, sender,
                Transaction.category.DEBIT, -amount);
        Transaction credit = new Transaction(id, recipient, sender,
                Transaction.category.CREDIT, amount);
        // The current constructor ignores its ID argument; pair the records explicitly.
        debit.setId(id.toString());
        credit.setId(id.toString());

        historyFor(senderId).add(debit);
        historyFor(recipientId).add(credit);
        sender.set(sender.getBalance() - value);
        recipient.set(recipient.getBalance() + value);
    }

    public Transaction[] getTransactionsFor(int userId) {
        return historyFor(userId).ToArray();
    }

    public void removeTransaction(int userId, String transactionId) {
        // Removing a history entry does not reverse the transfer.
        historyFor(userId).removeById(transactionId);
    }

    public Transaction[] checkValidity() {
        TransactionsList unpaired = new TransactionsLinkedList();
        for (int i = 0; i < usersList.RetrieveTheNumberOfUsers(); i++) {
            User user = usersList.RetrieveUserbyIndex(i);
            for (Transaction transaction : getTransactionsFor(user.getID())) {
                if (!hasMatchingPair(transaction)) {
                    unpaired.add(transaction);
                }
            }
        }
        return unpaired.ToArray();
    }

    private TransactionsList historyFor(int userId) {
        usersList.RetrieveUserById(userId);
        for (History current = histories; current != null; current = current.next) {
            if (current.userId == userId) {
                return current.transactions;
            }
        }
        histories = new History(userId, histories);
        return histories.transactions;
    }

    private boolean hasMatchingPair(Transaction transaction) {
        User other = transaction.getTRansferCategory() == Transaction.category.DEBIT
                ? transaction.getRecepient() : transaction.getSender();
        for (Transaction candidate : getTransactionsFor(other.getID())) {
            if (candidate != transaction
                    && candidate.getId().equals(transaction.getId())
                    && candidate.getTRansferCategory() != transaction.getTRansferCategory()
                    && candidate.getAmount() == -transaction.getAmount()
                    && candidate.getSender().getID() == transaction.getSender().getID()
                    && candidate.getRecepient().getID() == transaction.getRecepient().getID()) {
                return true;
            }
        }
        return false;
    }
}

// Kept here to limit changes to the two requested files.
class IllegalTransactionException extends RuntimeException {
    public IllegalTransactionException(String message) {
        super(message);
    }
}
