package school42.spring.application;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import school42.spring.printers.Printer;

public class Main {
    public static void main(String[] args) {
        try (ClassPathXmlApplicationContext context =
                 new ClassPathXmlApplicationContext("context.xml")) {

            Printer prefixPrinter =
                context.getBean("printerWithPrefix", Printer.class);

            Printer dateTimePrinter =
                context.getBean("printerWithDateTime", Printer.class);

            prefixPrinter.print("Hello Spring");
            dateTimePrinter.print("Hello Spring");
        }
    }
}