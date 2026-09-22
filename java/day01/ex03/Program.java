import java.util.UUID;

public class Program {
    public static void main(String[] args) {
        UsersList usersList = new UsersArrayList();

        usersList.AddUser(new User("John", 1000));
        usersList.AddUser(new User("Mike", 500));
        usersList.AddUser(new User("Anna", 200));

        System.out.println("Total users: " + usersList.RetrieveTheNumberOfUsers());

        for (int i = 0; i < usersList.RetrieveTheNumberOfUsers(); i++) {
            System.out.println(usersList.RetrieveUserbyIndex(i));
        }

        User found = usersList.RetrieveUserById(2);
        System.out.println("Found by id=2: " + found);

        // Trigger the exception on purpose
        User missing = usersList.RetrieveUserById(999);
    }
}
