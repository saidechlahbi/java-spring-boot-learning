import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("-> ");
        int number = scanner.nextInt();

        if (number <= 1) {
            System.err.println("IllegalArgument");
            System.exit(-1);
        }

        long i = 2;
        int steps = 0;
        boolean prime = true;
        while (i * i <= number)
        {
            steps++;
            if(number%i == 0)
            {
                prime = false;
                break;
            }
            i++;
        }
        if (prime)
        {
            steps++;
        }

        System.out.println(prime + " " + steps);
    }
}