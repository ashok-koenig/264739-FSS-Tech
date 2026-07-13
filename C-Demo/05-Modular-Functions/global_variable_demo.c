#include <stdio.h>

int x = 100;  // Global variable

void display() {
    int x=10; // Shadowing Variable
    printf("x = %d\n", x);
}

int main() {
    display();
    printf("x = %d\n", x);
    return 0;
}
