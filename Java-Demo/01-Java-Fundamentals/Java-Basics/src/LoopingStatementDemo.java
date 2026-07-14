import java.util.Scanner;

public class LoopingStatementDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of orders: ");
        int num = scanner.nextInt();
        int[] orderAmounts = new int[num];

        // Input the order amounts using for loop
        for(int index=0; index < num; index++){
            System.out.print("Enter amount for order "+(index +1)+ " :");
            orderAmounts[index]=scanner.nextInt();
        }

        // Enhanced for loop for array iteration
        System.out.println("Given order amounts:");
        for(int amount: orderAmounts){
            System.out.print(amount +"\t");
        }

        // Process the order and validate using while loop
        System.out.println("Processing order using while Loop: ");
        int index = 0;
        while(index<num){
            if(orderAmounts[index]>0){
                System.out.println("Order processed with amount: "+ orderAmounts[index]);
            }else{
                System.out.println("Invalid order amount: "+ orderAmounts[index]);
                orderAmounts[index]=0;
            }
            index++;
        }

        // Calculate the order total using do-while loop
        index=0;
        int total =0;
        do{
            total += orderAmounts[index]; // total = total + orderAmounts[index];
            index++;
        }while (index<num);
        System.out.println("Total order amount: "+ total);
    }
}
