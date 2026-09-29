


class mythread extends Thread 
{
    private final int count;
    private final String msg;
    public mythread(int count, String msg)
    {
        this.count = count;
        this.msg = msg;
    }
    @Override
    public void run()
    {
        for (int i = 0; i < count;i++)
        {
            System.out.println(msg);
        }
    }
}


// class Egg extends Thread{
//     @Override
//     public void run()
//     {
//         for (int i = 0; i < 5; i++)
//             System.out.println("Egg");
//     }
// }

// class Hen extends Thread{
//     @Override
//     public void run()
//     {
//         for (int i = 0; i < 5; i++)
//             System.out.println("Hen");
//     }
// }

public class Program{
    public static void main(String[] arg) throws InterruptedException
    {
        int count;
        if (arg.length != 1 || !arg[0].startsWith("--count="))
                return;
        try {
            count = Integer.parseInt(arg[0].substring("--count=".length()));
            
        } catch (NumberFormatException e) {
            System.out.println("Error: count must be valid intiger");
            return;
        }
       
            mythread t1 = new mythread(count, "Egg");
            // Thread t1 = new Thread(egg);
            
            mythread t2 = new mythread(count, "Hen");
            // Thread t2 = new Thread(hen);

            t1.start();
            t2.start();
            t1.join();
            t2.join();
            for (int i = 0; i < count;i++)
            {
                System.out.println("Human");
            }
    }
}