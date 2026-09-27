import java.util.Scanner;

import java.io.FileReader;
import java.io.FileInputStream;

import  java.io.FileWriter;

import java.io.IOException;

public class Program{
    public static void main (String [] arg) throws IOException
    {
        
        Scanner keyboard = new Scanner(System.in);
        try (FileWriter result = new FileWriter("result.txt")){
    
                while (true)
                {
                    System.out.print("-> ");
                    String path = keyboard.nextLine();
                    if (path.equals("42")) {
                        break;
                    }
                    boolean match = true;


                    try (Scanner signatures = new Scanner(new FileReader("signatures.txt")))
                    {
                        while (signatures.hasNextLine()) {
                            String line = signatures.nextLine();
                            
                            String[] parts = line.split(",");
                            String name =  parts[0].trim();
                            String[] hexBytes = parts[1].trim().split("\\s+");
                            
                            match = true;
                    
                            try(FileInputStream file =  new FileInputStream(path)){
                                for (String hexByte: hexBytes)
                                {
                                    int expected = Integer.parseInt(hexByte, 16);   
                                    int actuale = file.read();
                                    if (actuale !=  expected)
                                    {
                                        match  = false;
                                        break;
                                    }
                                }
                                if (match == true)
                                {
                                    result.write(name+ System.lineSeparator());
                                    System.out.println("PROCESSED");
                                    match = true;
                                    break;
                    
                                }
                            }
            
                        }
            
                        if (!match)
                        {
                            System.out.println("UNDEFINED");
                        }
                    }







                }





        }


    }

}