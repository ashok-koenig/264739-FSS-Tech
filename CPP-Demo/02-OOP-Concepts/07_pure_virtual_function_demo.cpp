#include <iostream>
using namespace std;

class Shape {
public:
    virtual void draw() = 0;  // pure virtual
};

class Circle : public Shape {
public:
    void draw() override {
        cout << "Drawing Circle" << endl;
    }
};

class Rectangle : public Shape {
public:
    void draw() override {
        cout << "Drawing Rectangle" << endl;
    }
};

int main() {
    Shape* s;

    Circle c;
    Rectangle r;

    s = &c;
    s->draw();

    s = &r;
    s->draw();
}
