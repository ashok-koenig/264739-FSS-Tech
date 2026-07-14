class Student {
    // Atrributes
    private String name;
    private int age;
    // Default constructor
    Student(){
        name = "Unknown";
        age = 0;
    }
    // Parameterized constructor
    Student(String n, int a){
        name = n;
        age = a;
    }

    // Methods
    public void showDetails(){
        System.out.println("Name: "+ name);
        System.out.println("Age: "+ age);
    }
}

public class StudentDemo{
    public static void main(String[] args) {
        Student john = new Student("John Smith", 25); // Object creation
//        john.name = "John Smith"; // accessing attributes
//        john.age = 25;
        john.showDetails(); // accessing method

        Student unknown = new Student();
        unknown.showDetails();
    }
}
