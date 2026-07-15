package collection_demo;

import java.util.ArrayList;
import java.util.List;

class Book{
    int bookId;
    String title, author;
    double price;

    public Book(int bookId, String title, String author, double price) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bookId=" + bookId +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                '}';
    }
}

public class LibraryExample {
    public static void main(String[] args) {
//        ArrayList<Book> books = new ArrayList<>();
//        books.add(new Book(101, "Let us C", "John", 245.00));
//        books.add(new Book(102, "Let us C++", "Peter", 565.50));
//        books.add(new Book(103, "Java", "Smith", 786.50));
        List<Book> books = List.of(
                new Book(101, "Let us C", "John", 245.00),
                new Book(102, "Let us C++", "Peter", 565.50),
                new Book(103, "Java", "Smith", 786.50)
        );
        System.out.println("List of books: ");
        for(Book book: books){
            System.out.println(book);
        }
    }
}
