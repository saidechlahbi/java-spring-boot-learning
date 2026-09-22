public class UserIdsGenerator{
    private int lastId;
    private static UserIdsGenerator instance;

    private UserIdsGenerator()
    {
        lastId = 0;
    }
    public static UserIdsGenerator getInstance()
    {
        if (instance == null)
        {
            instance = new UserIdsGenerator();
        }
        return instance;
    }
    public int generateId()
    {
        lastId++;
        return lastId;
    }
}