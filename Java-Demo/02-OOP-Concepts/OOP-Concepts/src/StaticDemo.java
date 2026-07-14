public class StaticDemo {
    // Static variable
    static int objectCount;

    // Static block
    static {
        objectCount =0;
    }

    // static method
    static void showObjectCount(){
        System.out.println("Total objects created: "+ objectCount);
    }
    StaticDemo(){
        objectCount++;
    }

    public static void main(String[] args) {
//        StaticDemo.objectCount =0;
        StaticDemo.showObjectCount();
        StaticDemo obj1 = new StaticDemo();
        StaticDemo.showObjectCount();
        StaticDemo obj2 = new StaticDemo();
        StaticDemo.showObjectCount();
    }
}
