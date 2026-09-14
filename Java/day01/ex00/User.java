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
    }
    ~User();
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
        return identifier;
    }
    public String getNAME()
    {
        return Name;
    }
    public long getBalance()
    {
        return Balance;
    }

}