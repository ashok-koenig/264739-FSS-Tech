//import mypackage.AnotherClass;
//import mypackage.MyClass;

import mypackage.*;
import another_package.*;

public class PackageDemo {
    public static void main(String[] args) {
        mypackage.MyClass obj = new mypackage.MyClass();
        AnotherClass anotherObj = new AnotherClass();
        another_package.MyClass obj2 = new another_package.MyClass();

    }
}
