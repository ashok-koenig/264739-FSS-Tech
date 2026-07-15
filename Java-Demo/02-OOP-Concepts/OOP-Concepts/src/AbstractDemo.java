abstract class Vehicle {
    String brand = "Generic";

    // Abstract method (no body)
    abstract void start();

    // Concrete method
    void stop(){
        System.out.println(brand + " Vehicle Stopped");
    }
}

class Car extends Vehicle{
    Car(String brand){
        super.brand = brand;
    }
    // implementing abstract method
    void start() {
        System.out.println(brand + " car started");
    }
}

// Interface
interface GPS{
    void navigate(String location);
}

class SmartCar extends Car implements GPS{
    SmartCar(String brand){
        super(brand);
    }
    public void navigate(String location) {
        System.out.println(brand + " smart car navigate to "+ location);
    }
}
class Bus extends Vehicle{
    Bus(String brand){
        super.brand = brand;
    }
    // implementing abstract method
    void start() {
        System.out.println(brand + " bus started");
    }
}
public class AbstractDemo {
    public static void main(String[] args) {
//        Vehicle obj = new Vehicle();
//        Car car1 = new Car("Tesla");
        Vehicle obj = new Car("Tesla");
        obj.start();
        obj.stop();

        obj = new Bus("Volvo");
        obj.start();
        obj.stop();

        SmartCar car = new SmartCar("Tesla");
        car.start();
        car.navigate("OMR");
        car.stop();
    }
}
