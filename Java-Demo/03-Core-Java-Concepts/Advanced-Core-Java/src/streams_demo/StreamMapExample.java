package streams_demo;

import java.util.List;

public class StreamMapExample {
    public static void main(String[] args) {
        List<Integer> numbers = List.of(10, 20, 30, 40 ,50);

        List<Integer> result = numbers.stream().map( num -> num / 10).toList();

        result.forEach(System.out::println);
    }
}
