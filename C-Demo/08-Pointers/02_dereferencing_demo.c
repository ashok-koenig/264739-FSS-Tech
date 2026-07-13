#include <stdio.h>

int main() {
    int x = 20;
    int *p = &x;

    printf("Address of p = %p\n", &p);
    printf("Address of x = %p\n", &x);
    printf("Value of x = %d\n", x);
    printf("Value using pointer = %d\n", *p);

    return 0;
}
