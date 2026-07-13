#include <iostream>
using namespace std;

int main() {
    int a = 10;
    int& ref = a;   // reference to a

    ref = 20;

    cout << "a = " << a << " " << &a << endl;     // 20
    cout << "ref = " << ref << " " << &ref << endl; // 20
}
