package collection_demo;

import java.util.ArrayList;

public class ArrayListExample {
    public static void main(String[] args) {
        // String array list
        ArrayList<String> fruits = new ArrayList<>();
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Orange");

        System.out.println("Fruits: "+ fruits);
        System.out.println("First Fruit: " + fruits.get(0));

        // Integer array list
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(100);
        numbers.add(200);
        numbers.add(300);
        System.out.println("Numbers: " + numbers);
        System.out.println("Number of elements: "+ numbers.size());

        Integer sum =0;
        for(Integer num: numbers){
            sum +=num;
        }
        System.out.println("Sum is "+ sum);
    }
}
