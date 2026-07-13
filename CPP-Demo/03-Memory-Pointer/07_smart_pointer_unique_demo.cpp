#include <iostream>
#include <memory>
using namespace std;

class Engine {
public:
    Engine() { cout << "Engine created\n"; }
    ~Engine() { cout << "Engine destroyed\n"; }
};

class Car {
private:
    unique_ptr<Engine> engine;

public:
    Car() {
        engine = make_unique<Engine>();
    }
};


int main() {
    unique_ptr<int> p = make_unique<int>(10);

    cout << *p << endl;  // 10

    Car c;
}
