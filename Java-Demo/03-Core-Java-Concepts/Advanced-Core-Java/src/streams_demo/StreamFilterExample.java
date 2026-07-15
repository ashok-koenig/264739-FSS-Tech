package streams_demo;

import java.util.List;
import java.util.stream.Stream;

public class StreamFilterExample {
    public static void main(String[] args) {
        List<String> names = List.of("John", "Peter", "Smith", "Jane");
//        Stream<String> nameStream = names.stream();
//        nameStream.filter(name -> name.startsWith("J"))
//                .forEach(name -> System.out.println(name));

        names.stream().filter(name -> name.startsWith("J"))
               .forEach(System.out::println);
    }
}
