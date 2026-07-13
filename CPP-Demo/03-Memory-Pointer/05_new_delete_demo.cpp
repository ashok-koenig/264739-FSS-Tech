#include <iostream>
using namespace std;

int main() {
    int* p = new int(50);

    cout << "Value: " << *p << endl;

    delete p;  // free memory
}
