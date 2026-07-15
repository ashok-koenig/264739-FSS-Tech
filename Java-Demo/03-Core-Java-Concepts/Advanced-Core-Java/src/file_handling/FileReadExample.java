package file_handling;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReadExample {
    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader("example.txt");
            int ch;
            while ((ch = fr.read()) != -1) {
                System.out.print((char)ch);
            }
            fr.close();
        } catch (FileNotFoundException e) {
            System.out.println("File read Exception: "+ e.getMessage());
        }catch (IOException e) {
            System.out.println("File read Exception: "+ e.getMessage());
        }
    }
}
