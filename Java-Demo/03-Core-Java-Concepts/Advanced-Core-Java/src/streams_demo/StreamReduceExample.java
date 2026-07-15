package streams_demo;

import java.util.List;

public class StreamReduceExample {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(10, 20, 30, 40 ,50);

        int sum = numbers.stream().reduce(0, (a, b)-> a + b);

        System.out.println("Sum: " + sum);
    }
}
