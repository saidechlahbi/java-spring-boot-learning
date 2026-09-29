class SharedPrinter {
    private boolean eggTurn = true;

    public synchronized void print(String message, boolean isEgg)
            throws InterruptedException {

        while (eggTurn != isEgg) {
            wait();
        }

        System.out.println(message);

        eggTurn = !eggTurn;

        notifyAll();
    }
}

class MyThread extends Thread {
    private final int count;
    private final String message;
    private final boolean isEgg;
    private final SharedPrinter printer;

    public MyThread(int count, String message, boolean isEgg,
                    SharedPrinter printer) {
        this.count = count;
        this.message = message;
        this.isEgg = isEgg;
        this.printer = printer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < count; i++) {
                printer.print(message, isEgg);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

public class Program {
    public static void main(String[] args) throws InterruptedException {
        if (args.length != 1 || !args[0].startsWith("--count=")) {
            System.out.println("Usage: java Program --count=<number>");
            return;
        }

        int count;

        try {
            count = Integer.parseInt(
                args[0].substring("--count=".length())
            );
        } catch (NumberFormatException e) {
            System.out.println("Error: count must be a valid integer.");
            return;
        }

        if (count < 0) {
            System.out.println("Error: count must not be negative.");
            return;
        }

        SharedPrinter printer = new SharedPrinter();

        MyThread egg = new MyThread(count, "Egg", true, printer);
        MyThread hen = new MyThread(count, "Hen", false, printer);

        egg.start();
        hen.start();

        egg.join();
        hen.join();
    }
}