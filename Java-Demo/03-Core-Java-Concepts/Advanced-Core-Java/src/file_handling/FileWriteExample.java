package file_handling;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriteExample {
    public static void main(String[] args) {
        try {
            FileWriter fw = new FileWriter("example.txt");
            fw.write("Hello from Java FileWriter!");
            fw.close();
            System.out.println("File written successfully.");
        } catch (IOException e) {
            System.out.println("File Write Exception: "+ e.getMessage());
        }
    }
}
