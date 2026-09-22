/**
 * UsersList
 */
public interface UsersList {
    public void AddUser(User user);
    public User RetrieveUserById(int id);
    public User RetrieveUserbyIndex(int index);
    public int RetrieveTheNumberOfUsers();
}