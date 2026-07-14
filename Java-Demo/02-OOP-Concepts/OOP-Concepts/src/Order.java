import java.util.Scanner;

class Item{
    String name;
    int quantity;
    Item(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
    }
    void showItem(){
        System.out.println("Name: "+ name + ", Quantity: "+ quantity);
    }
}
public class Order {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of items: ");
        int numberOfItems = scanner.nextInt();
        Item[] items = new Item[numberOfItems];
        for(int i=0;i<numberOfItems;i++){
            System.out.println("Enter name and quantity of item "+(i+1)+":");
            String name = scanner.next();
            int quantity = scanner.nextInt();
            items[i]= new Item(name, quantity);
        }
        System.out.println("Item Details:");
        for(Item item: items){
            item.showItem();
        }
    }
}
