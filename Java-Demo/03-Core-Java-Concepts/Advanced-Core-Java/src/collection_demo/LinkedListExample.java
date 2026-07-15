package collection_demo;

import java.util.LinkedList;

public class LinkedListExample {
    public static void main(String[] args) {
        LinkedList<String> tasks = new LinkedList<>();
        tasks.add("Task 1");
        tasks.add("Task 2");
        tasks.add("Task 3");
        // Queue -- FIFO
        System.out.println("Tasks: "+ tasks);
        // Process task
        while (!tasks.isEmpty()){
            System.out.println("Processing: "+ tasks.poll());
        }
    }
}
