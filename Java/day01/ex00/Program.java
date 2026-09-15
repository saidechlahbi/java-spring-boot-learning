import java.util.UUID;


public class Program {
    public static void main(String[] args) {
        User john = new User(1, "John", 1000);
        User mike = new User(2, "Mike", 500);

        System.out.println(john);
        System.out.println(mike);

        
        Transaction outcome = new Transaction(UUID.randomUUID() ,mike, john, Transaction.category.DEBIT, -500);
        Transaction income = new Transaction(UUID.randomUUID(), mike, john, Transaction.category.CREDIT, 500);

        System.out.println(outcome);
        System.out.println(income);
    }
}
