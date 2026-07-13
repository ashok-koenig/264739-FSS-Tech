#include <stdio.h>
#include <string.h>

int main() {
    char str1[20] = "Hello";
    char str2[20];

    // Copy
    strcpy(str2, str1);

    // Length
    printf("Length: %d\n", strlen(str1));

    // Compare
    if(strcmp(str1, str2) == 0) {
        printf("Strings are equal\n");
    }

    return 0;
}
