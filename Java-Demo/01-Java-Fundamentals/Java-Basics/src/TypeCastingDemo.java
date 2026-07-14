public class TypeCastingDemo {
    public static void main(String[] args) {

        // Widening Casting (Automatic)
        int intVal = 100;
        long longVal = intVal;
        double doubleVal = longVal;

        System.out.println("Widening Casting: ");
        System.out.println("int -> long: "+ longVal);
        System.out.println("long -> double: "+ doubleVal);

        // Narrowing Casting (Manual)
        double originalDouble = 123.456;
        int castedInt = (int) originalDouble;

        System.out.println("Narrowing Casting:");
        System.out.println("double: "+ originalDouble);
        System.out.println("double -> int: "+ castedInt);
    }
}
