#include <stdio.h>

// Function Prototype
int multiply(int, int);

int main() {
    int result = multiply(4, 5);
    printf("Result = %d", result);
    return 0;
}

// Function Definition
int multiply(int a, int b) {
    return a * b;
}
