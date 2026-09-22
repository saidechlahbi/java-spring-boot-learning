public class TransactionsLinkedList implements TransactionsList
{
    private static class Node
    {
        Transaction value;
        Node next;
        node (Transaction value)
        {
            this.value = value;
        }
    }
}