#include <iostream>
using namespace std;

class BankAccount {
private:
    double balance;   // hidden data

public:
    void setBalance(double b) {
        balance = b;
    }

    double getBalance() {
        return balance;
    }
};

int main() {
    BankAccount acc;

    acc.setBalance(5000);         // allowed
    cout << acc.getBalance();     // allowed

    // acc.balance = 1000; // ERROR (private)
}
