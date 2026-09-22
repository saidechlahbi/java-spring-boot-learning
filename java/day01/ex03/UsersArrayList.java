
public class UsersArrayList implements UsersList{
    private User[] users;
    private int size;

    public UsersArrayList()
    {
        users = new User[10];
        size = 0;
    }
    @Override 
    public void AddUser(User user)
    {
        if (users.length == size)
        {
            grow();
        }
        users[size] = user;
        size++;
    }
    @Override 
    public User RetrieveUserById(int id)
    {
        for (int i = 0; i < size;i++)
        {
            if (users[i].getID() == id)
                    return  users[i];
        }
        throw new UserNotFoundException("user with id = " + id + " not found");
    }
    @Override 
    public User RetrieveUserbyIndex(int index)
    {
        if (index < 0 || index >= size)
            throw new UserNotFoundException("user with index = " + index + " not found");
        return users[index];
    }
    @Override 
    public int RetrieveTheNumberOfUsers()
    {
        return size;
    }

    private void grow(){
        int newSize = size + (users.length / 2);
        User [] buffer = users;
        users = new User[newSize];
        for (int i = 0; i < size; i++)
        {
            users[i] = buffer[i];
        } 
    }
}