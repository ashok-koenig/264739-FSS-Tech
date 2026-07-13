#include <stdio.h>

void display() {
    int x = 10;  // Local variable
    printf("x = %d\n", x);
}

int main() {
    display();
    // printf("%d", x); // ❌ Error: x not accessible here
    return 0;
}
