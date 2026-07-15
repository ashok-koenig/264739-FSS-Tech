interface SimpleCalculator {
    int add(int n1, int n2);
    int sub(int n1, int n2);
    int mul(int n1, int n2);
    default void welcome(){
        System.out.println("This is a default method in interface");
    }
}
public class InterfaceDemo implements SimpleCalculator{

    public int add(int n1, int n2) {
        return n1+n2;
    }

    public int sub(int n1, int n2) {
        return n1-n2;
    }

    public int mul(int n1, int n2) {
        return n1*n2;
    }

    @Override
    public void welcome() {
        System.out.println("Interface default welcome method overridden");
    }

    public static void main(String[] args) {
        SimpleCalculator cal = new InterfaceDemo();
        System.out.println("100 + 20 : "+ cal.add(100,20));
        cal.welcome();
    }
}
