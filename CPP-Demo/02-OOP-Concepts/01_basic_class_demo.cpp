#include <iostream>
using namespace std;

class Student {
public:
    string name;
    int age;

    void display() {
        cout << "Name: " << name << ", Age: " << age;
    }
};

int main() {
    Student s1;      // object creation

    s1.name = "Ashok";
    s1.age = 30;

    s1.display();    // calling method
    return 0;
}
