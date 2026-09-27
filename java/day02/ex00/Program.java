
import java.io.FileInputStream;

import java.io.IOException;

public class Program{
    public static void main (String [] arg) throws IOException
    {
        FileInputStream file = new  FileInputStream("image.png");
        int first = file.read();
        int second  = file.read();
        file.close();
        System.out.println(first);
        System.out.println(second);
    }
}