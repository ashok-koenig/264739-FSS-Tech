class Person {
    String name;
    int age;
    Person(String name, int age){
        this.name = name;
        this.age = age;
    }
    void introduce(){
        System.out.println("I'm "+ name + " and "+ " years old");
    }
}

class Manager extends Person {
    String department;
    Manager(String name, int age, String department){
        super(name, age);
        this.department = department;
    }
    // Overriding
    void introduce(){
        super.introduce();
        System.out.println("Department is "+ department);
    }
}
public class InheritanceDemo {
    public static void main(String[] args) {
        Manager smith = new Manager("Smith", 30, "IT");
        smith.introduce();
    }
}
