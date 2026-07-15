package collection_demo;

import java.util.HashMap;

public class MapExample {
    public static void main(String[] args) {
        HashMap<Integer, String> students = new HashMap<>();
        students.put(101, "John");
        students.put(102, "Bob");
        students.put(103, "Peter");
        students.put(104, "John");
        students.put(102, "Smith");
        System.out.println("Students: " + students);
        System.out.println("Student with Roll number 102: "+ students.get(102));

    }
}
