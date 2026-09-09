import java.util.Scanner;
public class Program
{
    public static void main(String [] args)
    {
        Scanner scanner = new Scanner(System.in);
        System.out.print("->  ");
        int number = scanner.nextInt();

        if (number <= 1)
        {
            System.err.println("IllegalArgument");
            System.exit(-1);
        }
        int steps = 0;
        for (int i = number / 2 ; i > 1; i--)
        {
            steps ++;
            if (number % i  == 0)
            {
                System.out.print("false "+steps);
                System.exit(0);
            }
        }


        System.out.print("true "+steps);

        scanner.close();
    }
}