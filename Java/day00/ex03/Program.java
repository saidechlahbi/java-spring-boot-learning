import java.util.Scanner;

public class Program {
    public static void main(String[] arg) {
        int order = 0;
        Scanner scanner = new Scanner(System.in);
        long weekdata = 0;

        for (int i = 0; i < 18; i++) {
            String token = scanner.next();
            if (token.equals("42")) {
                break;
            }
            int weeknumber = scanner.nextInt();
            if (weeknumber != order + 1) {
                System.err.println("IllegalArgument");
                System.exit(-1);
            }

            int min = 10;
            for (int j = 0; j < 5; j++) {
                int grade = scanner.nextInt();
                if (grade < min) {
                    min = grade;
                }
            }
            weekdata = weekdata * 10 + min;
            order += 1;
        }

        long reversedata = 0;
        long temp = weekdata;
        while (temp != 0) {
            reversedata = reversedata * 10 + (temp % 10);
            temp /= 10;
        }

        for (int i = 0; i < order; i++) {
            System.out.print("Week " + (i + 1) + " ");
            for (int j = 0; j < reversedata % 10; j++) {
                System.out.print("=");
            }
            System.out.println(">");
            reversedata /= 10;
        }
    }
}