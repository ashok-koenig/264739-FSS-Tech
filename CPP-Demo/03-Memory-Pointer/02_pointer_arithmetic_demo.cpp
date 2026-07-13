#include <iostream>
using namespace std;

int main() {
    int arr[] = {10, 20, 30};

    int* p = arr;  // points to first element

    cout << "Address of arr: " << arr << endl;
    cout << "Address of p: " << p << endl;

    cout << *p << endl;     // 10
    cout << *(p + 1) << endl; // 20
    cout << *(p + 2) << endl; // 30
}