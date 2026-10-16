// package java.day08.Spring-folder;
import java.util.Locale;

public interface PreProcessor {

    String process(String message);
}

public class PreProcessorToUpperImpl implements PreProcessor
{
    @override
    public string process(String message)
    {
        return message.toUpperCase(Locale, ROOT);
    }
}

public class main {
    public static void main (String arg[])
    {
        System.out.println("hello word");
    }
}
