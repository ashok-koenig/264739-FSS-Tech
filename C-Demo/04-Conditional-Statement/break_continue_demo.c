#include <stdio.h>

int main() {
    for (int i = 1; i <= 10; i++) {

        if (i % 2 == 0) {
            continue;  // Skip even numbers
        }

        if (i > 7) {
            break;     // Stop loop after 7
        }

        printf("%d ", i);
    }

    return 0;
}
