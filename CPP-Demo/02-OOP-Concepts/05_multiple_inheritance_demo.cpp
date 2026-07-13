#include <iostream>
using namespace std;

class Teacher {
public:
    void teach() {
        cout << "Teaching..." << endl;
    }
};

class Researcher {
public:
    void research() {
        cout << "Researching..." << endl;
    }
};

// Multiple inheritance
class Professor : public Teacher, public Researcher {
};

int main() {
    Professor p;

    p.teach();     // from Teacher
    p.research();  // from Researcher
}
