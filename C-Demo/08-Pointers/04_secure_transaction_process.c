#include <stdio.h>
#include <string.h>

void process(char type[], float *balance, float amount) {
    if(strcmp(type, "DEPOSIT") == 0)
        *balance += amount;
    else if(strcmp(type, "WITHDRAW") == 0)
        *balance -= amount;
    else
        printf("Invalid transaction\n");
}

int main() {
    float balance = 1000;

    process("DEPOSIT", &balance, 500);
    process("WITHDRAW", &balance, 200);

    printf("Final Balance: %.2f\n", balance);
    return 0;
}
