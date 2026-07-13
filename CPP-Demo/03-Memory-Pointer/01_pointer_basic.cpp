#include <iostream>
using namespace std;

int main() {
    int x = 10;

    int* ptr = &x;  // pointer stores address of x

    cout << "Value of x: " << x << endl;
    cout << "Address of x: " << &x << endl;
    cout << "Pointer value (address): " << ptr << endl;
    cout << "Value using pointer: " << *ptr << endl;
}
