import java.util.UUID;


public class Transaction
{
    public enum category
    { 
        DEBIT, CREDIT 
    }
    private String Identifier;
    private User Recipient;
    private User Sender;
    private category TransferCategory;

    private double Amount;

    public Transaction(UUID id, User recepient, User sender, category transferCategory, double amount)
    {
        if (transferCategory == category.DEBIT && amount >= 0) {
            throw new IllegalArgumentException("DEBIT amount must be negative");
        }
        if (transferCategory == category.CREDIT && amount <= 0) {
            throw new IllegalArgumentException("CREDIT amount must be positive");
        }

        Identifier = UUID.randomUUID().toString();
        Recipient = recepient;
        Sender = sender;
        TransferCategory = transferCategory;
        Amount = amount;
    }

    /*geters */
    public String getId(){
        return Identifier;
    }
    public User getRecepient(){
        return Recipient;
    }
    public User getSender(){
        return Sender;
    }
    public category getTRansferCategory(){
        return TransferCategory;
    }
    public double getAmount(){
        return Amount;
    }

    /*seters */
    public void setId(String id){
        Identifier = id;
    }
    public void setRecepient(User recepient){
        Recipient = recepient;
    }
    public void setSender(User sender){
        Sender = sender;
    }
    public void setTRansferCategory(category tr){
        TransferCategory = tr;
    }
    public void setAmount(double amount){
        Amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{id='" + Identifier + "', sender=" + Sender.getName()
                + ", recipient=" + Recipient.getName()
                + ", category=" + TransferCategory + ", amount=" + Amount + "}";
    }

}