
public class Program{

    public static void main (String [] args)
    {
        int a = 479598;
        int same = 0;
        for (int i = 0; i < 6; i++)
        {
            same = same + (a % 10);
            a = a / 10;
        }
        System.out.println(same);
    }
}