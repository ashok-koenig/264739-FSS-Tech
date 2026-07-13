#include <stdio.h>

int main() {
    int id;
    float marks;
    char section;

    printf("Enter ID, Marks and Section: ");
    scanf("%d %f %c", &id, &marks, &section);

    printf("\nStudent Details:\n");
    printf("ID: %d\n", id);
    printf("Marks: %.2f\n", marks);
    printf("Section: %c\n", section);

    return 0;
}
