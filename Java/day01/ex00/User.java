public class User
{
    private int Identifier;
    private String Name;
    private long Balance;

    public User(int id, String name, long balance)
    {
        if (balance < 0)
        {
            throw new IllegalArgumentException("Initial balance cannot be negative");
        }
        Identifier = id;
        Name = name;
        Balance = balance;
    }
    public void setID(int id)
    {
        Identifier = id;
    }
    public void setNAME(String name)
    {
        Name = name;
    }   
    public void set(long balance)
    {
        Balance = balance;
    }

    public int getID()
    {
        return Identifier;
    }
    public String getName()
    {
        return Name;
    }
    public long getBalance()
    {
        return Balance;
    }

    @Override 
    public String toString() {
        return "User{id=" + Identifier + ", name='" + Name + "', balance=" + Balance + "}";
    }
}