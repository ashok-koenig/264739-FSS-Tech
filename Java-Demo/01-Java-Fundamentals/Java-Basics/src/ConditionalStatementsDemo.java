import java.util.Scanner;

public class ConditionalStatementsDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Inter a number
        System.out.println("Enter a number: ");
        int num = scanner.nextInt();

        System.out.println("Entered number is :"+ num);

        // if statement
        if(num > 0){
            System.out.println("The number is positive");
        }

        // if-else statement
        if(num % 2 == 0){
            System.out.println("The number is even");
        }else{
            System.out.println("The number is Odd");
        }

        // nested if-else statement
        if(num >=0){
            if(num ==0){
                System.out.println("The number is zero.");
            }else{
                System.out.println("The number is greater than zero.");
            }
        }else{
            System.out.println("The number os less than zero");
        }

        // switch-case statement
        System.out.println("Enter a day number (1-7):");
        int day = scanner.nextInt();

        System.out.println("Day of the week: ");
        switch (day){
            default:
                System.out.println("Invalid day number");
                break;
            case 1:
                System.out.println("Sunday");
                break;
            case 2:
                System.out.println("Monday");
                break;
            case 3:
                System.out.println("Tuesday");
                break;
            case 4:
                System.out.println("Wednesday");
                break;
            case 5:
                System.out.println("Thursday");
                break;
            case 6:
                System.out.println("Friday");
                break;
            case 7:
                System.out.println("Saturday");
                break;

        }
        scanner.close();
    }
}
