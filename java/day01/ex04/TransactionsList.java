/**
 * TransactionsList
 */
public interface TransactionsList {

    void add (Transaction transaction);
    void removeById(String id);
    Transaction[] ToArray();
}