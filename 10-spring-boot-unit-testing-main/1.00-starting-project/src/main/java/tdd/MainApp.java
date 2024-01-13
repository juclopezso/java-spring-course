package tdd;

public class MainApp {

    public static void main(String[] args) {
        for (int i = 0; i<= 100; i++) {
            System.out.println(i + "," + FizzBuzz.compute(i));
        }
    }

}
