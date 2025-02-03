import java.util.Scanner;

public class Viktor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("_________________________________________");
        System.out.println("Hi there. I'm Viktor.");
        System.out.println("How can I help you?");
        System.out.println("_________________________________________");

        // Simulating simple interaction
        String input = scanner.nextLine();

        System.out.println("_________________________________________");
        System.out.println("Bye. Hope to see you again soon/");
        System.out.println("_________________________________________");

        scanner.close();
    }
}