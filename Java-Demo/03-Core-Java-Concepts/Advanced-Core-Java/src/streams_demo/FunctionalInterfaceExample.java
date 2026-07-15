package streams_demo;

interface SimpleInterface {
    void display();
}

//class Demo implements SimpleInterface{
//    public void display() {
//        System.out.println("Implementation of display method");
//    }
//}

@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

public class FunctionalInterfaceExample {
    public static void main(String[] args) {
//        Demo demo = new Demo();
//        demo.display();
        SimpleInterface demo = () -> System.out.println("Implementation of display method");
        demo.display();

        MathOperation addition = (x, y) -> x + y;
        System.out.println("Sum: "+ addition.operate(10,20));

        MathOperation multiplication = (x, y ) -> x * y;
        System.out.println("Product: "+ multiplication.operate(100, 34));
    }
}
