public class TransactionsLinkedList implements TransactionsList
{
    private static class Node
    {
        Transaction value;
        Node next;
        Node (Transaction value)
        {
            this.value = value;
        }
    }
    private Node head;
    private Node tail;
    private int size;

    @Override
    public void add (Transaction transaction)
    {
        Node tmp = new Node(transaction);
        if (head == null){
            head = tmp;
            tail = tmp;
        }else{
            tail.next = tmp;
            tail = tmp;
        }  
        size++;
    }

    @Override
    public void removeById(String id)
    {
        Node current = head;
        Node previous = null;
        while(current != null)
        {
            if (current.value.getId().equals(id))
            {
                if (previous == null)   
                    head = current.next;
                else
                    previous.next = current.next;
                if (current == tail)
                    tail = previous;
                size--;
                return;
            }
            previous = current;
            current = current.next;
        }
        throw new UserNotFoundException("Transaction with id = " + id + "not found");
    }

    @Override
    public Transaction[] ToArray()
    {
        Transaction[] array = new Transaction[size];
        if (head == null )
            return array;
        Node current = head;
        for (int i = 0; i < size ; i++)
        {
            array[i] = current.value;
            current = current.next;
        }
        return array;

    }

}