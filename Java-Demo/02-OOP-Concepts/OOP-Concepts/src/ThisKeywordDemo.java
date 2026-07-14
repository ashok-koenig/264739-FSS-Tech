class Employee {
    String name;
    double salary;
    Employee(){
        this("Unknown", 0);
    }
    Employee(String name, double salary){
        this.name = name;
        this.salary = salary;
    }
    void showDetails(){
        System.out.println("Employee Introduction:");
        this.introduceEmployee();
    }
    void introduceEmployee(){
        System.out.println("I'm "+ this.name + " and my salary is "+ this.salary);
    }
}
public class ThisKeywordDemo {
    public static void main(String[] args) {
        Employee emp = new Employee("Peter", 12333);
        emp.showDetails();
    }
}
