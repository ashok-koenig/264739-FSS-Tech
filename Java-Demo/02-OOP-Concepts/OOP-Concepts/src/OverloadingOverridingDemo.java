class Calculator{
    int add(int n1, int n2){
        return n1 + n2;
    }
    // Method overloading
    int add(int n1, int n2, int n3){
        return n1 + n2 + n3;
    }
    String whoAmI(){
        return "I'm simple calculator";
    }
}

class AdvancedCalculator extends Calculator{
    double power(double x, double y){
        return Math.pow(x, y);
    }
    // Method overriding
    String whoAmI(){
        return "I'm Advanced calculator";
    }
}
public class OverloadingOverridingDemo {
    public static void main(String[] args) {
//        Calculator calc = new Calculator();
        AdvancedCalculator calc = new AdvancedCalculator();
        System.out.println("Add 2 numbers: "+ calc.add(100, 200));
        System.out.println("Add 3 numbers: "+ calc.add(100, 200, 300));
        System.out.println(calc.whoAmI());
        System.out.println("Power of 10 to 5: "+ calc.power(10, 5));
    }
}
