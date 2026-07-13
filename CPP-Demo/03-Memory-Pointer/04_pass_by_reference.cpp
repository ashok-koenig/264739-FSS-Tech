#include <iostream>
using namespace std;

void update(int& x) {
    x = 100;
}

int main() {
    int a = 10;
    update(a);

    cout << a;  // Output: 100
}
