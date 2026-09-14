import java.util.UUID;
public class User;
public class Transaction
{
    public enum category
    { 
        DEBIT, CREDIT 
    }
    private UUID Identifier;
    private User Recipient;
    private User Sender;
    private category TransferCategory;

    private double Amount;

    public Transaction(UUID id, User recepient, User sender, category TransferCategory, double amount)
    {
        
    }

}