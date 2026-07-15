package exception_demo;

public class ExceptionExample {
    public static void main(String[] args) {
        int n1 = 10;
        int n2 = 0;
//        int n2 = 2;
        int result = 0;
//        String str = "Welcome";
        String str = null;
        try {
            System.out.println("String length: "+ str.length());
            result = n1 / n2;
            System.out.println("Result: " + result);
        }catch (ArithmeticException e){
            System.out.println("ArithmeticException: " + e.getMessage());
        }catch (NullPointerException e){
            System.out.println("NullPointerException: " + e.getMessage());
        } catch (Exception e){
            System.out.println("Exception: "+ e.getMessage());
        }finally {
            System.out.println("Execution finished");
        }


    }


}
