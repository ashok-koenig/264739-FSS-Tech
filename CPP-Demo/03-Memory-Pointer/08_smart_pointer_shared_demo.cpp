#include <iostream>
#include <memory>
using namespace std;

int main() {
    shared_ptr<int> p1 = make_shared<int>(100);
    shared_ptr<int> p2 = p1;

    cout << p1.use_count() << endl;  // 2
}
