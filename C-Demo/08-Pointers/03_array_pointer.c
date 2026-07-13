#include<stdio.h>

int main(){
    int arr[]={1,2,3,4};

    printf("arr[0] = %d \n", arr[0]);
    printf("*arr = %d \n", *arr);

    printf("arr[1] = %d \n", arr[1]);
    printf("*(arr+1) = %d \n", *(arr+1));

    // arr[1]=20;
    *(arr+1) = 20;

    printf("arr[1] = %d \n", arr[1]);
    printf("*(arr+1) = %d \n", *(arr+1));
}