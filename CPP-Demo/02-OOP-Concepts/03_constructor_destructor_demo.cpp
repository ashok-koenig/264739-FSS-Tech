#include <iostream>
using namespace std;

class FileHandler {
private:
    string filename;

public:
    FileHandler(string name) : filename(name) {
        cout << "Opening file: " << filename << endl;
    }

    ~FileHandler() {
        cout << "Closing file: " << filename << endl;
    }
};

int main() {
    FileHandler f("data.txt");
    return 0;
}
