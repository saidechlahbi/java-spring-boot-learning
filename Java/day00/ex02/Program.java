import java.util.Scanner;
public class Program{
    public static void main (String [] args)
    {
        int number = 0;
        int request = 0;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.print("-> ");
            number  = scanner.nextInt();
            


            int variable = 0;
            int a = number;

            while(a!=0)
            {
                variable += a%10;
                a = a/10;
            }

            long i = 2;
            boolean prime = true;
            while (i * i <= variable)
            {
                if(variable%i == 0)
                {
                    prime = false;
                    break;
                }
                i++;
            }

            if (prime)
                request++;

        }while(number != 42);
        
        System.out.println("Count of coffee-request : "+request);
        
    }
}