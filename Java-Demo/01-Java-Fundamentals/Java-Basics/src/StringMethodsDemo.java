public class StringMethodsDemo {
    public static void main(String[] args) {
        // Creating String
        String firstName = "John";
        String lastName = new String("Smith");

        System.out.println("First name: " + firstName);
        System.out.println("last name: "+ lastName);

        // String methods
        System.out.println("Length of first name: " + firstName.length());

        String fullName = firstName + " " + lastName;
//        String fullName = firstName.concat(" ").concat(lastName);

        System.out.println("Full name: " + fullName);

        String text = "Working with Java String methods";

        System.out.println("Substring of text from index 0 to 12: " + text.substring(0,12));
        System.out.println("Substring of text from index 12: " + text.substring(12));

        String[] words = text.split(" ");
        System.out.println("Words:");
        for(String word: words){
            System.out.println(word);
        }

        String name1 = "Peter";
        String name2 = "peter";

        System.out.println("name1 equals name2: "+ name1.equals(name2));
        System.out.println("name1 equals name2 ignore case: "+ name1.equalsIgnoreCase(name2));

        String username = "        admin       ";
        System.out.println("Original username: '" + username + "'" );
        username = username.trim();
        System.out.println("After trimming username: '" + username + "'" );

    }
}
