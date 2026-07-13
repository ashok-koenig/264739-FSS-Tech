#include <iostream>
using namespace std;

int main() {
    string name;
    int age;

    cout << "Enter name: ";
    // cin >> name;
    getline(cin, name);

    cout << "Enter age: ";
    cin >> age;

    cout << "Hello " << name << ", Age: " << age;
    return 0;
}
