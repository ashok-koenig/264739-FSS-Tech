public class OperatorPrecedenceDemo {
    public static void main(String[] args) {

        int a = 5, b = 10, c = 15;

        // Operator Precedence
        int result1 = a + b * c;
        int result2 = (a + b ) * c;
        System.out.println("a + b * c = "+ result1);
        System.out.println("(a + b) * c = " + result2);

        // Unary Operators
        int x = 10;
        System.out.println("Unary Operators: ");
        System.out.println("Original x: " + x);
        System.out.println("Unary minus -x: " + (-x));
        System.out.println("Post-increment x++: " + (x++)); // prints 10, then x becomes 11
        System.out.println("After post increment: "+ x);
        System.out.println("Pre-increment ++x: "+ (++x)); // x becomes 12, then prints 12

        // Ternary Operator
        int marks = 75;
        String result = (marks >=50) ? "Pass" : "Fail";
        System.out.println("Ternary Operator: ");
        System.out.println("Marks: "+ marks);
        System.out.println("Result: "+ result);
    }
}
