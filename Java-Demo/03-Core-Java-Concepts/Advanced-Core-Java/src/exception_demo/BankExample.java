package exception_demo;

class InsufficientBalanceException extends Exception{
    public InsufficientBalanceException(String message){
        super(message);
    }
}

public class BankExample {
    private double balance;
    BankExample(double balance){
        this.balance = balance;
    }
    public void withdraw(double amount) throws InsufficientBalanceException{
        if(amount>balance){
            throw new InsufficientBalanceException("Insufficient balance");
        }
        balance -=amount;
        System.out.println("Withdrawal successful. Balance: "+ balance);
    }

    public static void main(String[] args) {
        BankExample account = new BankExample(500);
        try {
            account.withdraw(600);
        }catch (InsufficientBalanceException e){
            System.out.println("Exception: "+ e.getMessage());
//            account.withdraw(400);
        }
        System.out.println("Transaction Ends");
    }
}
