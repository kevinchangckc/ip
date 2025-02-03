import java.util.Scanner;

/*  level-0
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
        System.out.println("Bye. Hope to see you again soon.");
        System.out.println("_________________________________________");

        scanner.close();
    }
}
 */

/* level-1
public class Viktor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("_________________________________________");
        System.out.println("Hi there. I'm Viktor.");
        System.out.println("How can I help you?");
        System.out.println("_________________________________________");

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("_________________________________________");
                System.out.println("Bye. Hope to see you again soon.");
                System.out.println("_________________________________________");
                break;  // Exit the loop
            }

            System.out.println("_________________________________________");
            System.out.println(input);  // Echoes back user input
            System.out.println("_________________________________________");
        }

        scanner.close();
    }
}

 */


/* level-2
import java.util.ArrayList;
import java.util.Scanner;

public class Viktor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> tasks = new ArrayList<>();

        System.out.println("_________________________________________");
        System.out.println("Hi there. I'm Viktor.");
        System.out.println("How can I help you?");
        System.out.println("_________________________________________");

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("_________________________________________");
                System.out.println("Bye. Hope to see you again soon.");
                System.out.println("_________________________________________");
                break;
            } else if (input.equalsIgnoreCase("list")) {
                System.out.println("_________________________________________");
                System.out.println("Here are your tasks:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
                System.out.println("_________________________________________");
            } else {
                tasks.add(input);
                System.out.println("_________________________________________");
                System.out.println("Added: " + input);
                System.out.println("_________________________________________");
            }
        }

        scanner.close();
    }
}

 */


// level-3
import java.util.ArrayList;

class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsNotDone() {
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}

public class Viktor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();

        System.out.println("_________________________________________");
        System.out.println("Hi there. I'm Viktor.");
        System.out.println("How can I help you?");
        System.out.println("_________________________________________");

        while (true) {
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("bye")) {
                System.out.println("_________________________________________");
                System.out.println("Bye. Hope to see you again soon.");
                System.out.println("_________________________________________");
                break;
            } else if (input.equalsIgnoreCase("list")) {
                System.out.println("_________________________________________");
                System.out.println("Here are your tasks:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
                System.out.println("_________________________________________");
            } else if (input.startsWith("mark ")) {
                try {
                    int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                    tasks.get(taskIndex).markAsDone();
                    System.out.println("_________________________________________");
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println(tasks.get(taskIndex));
                    System.out.println("_________________________________________");
                } catch (Exception e) {
                    System.out.println("Invalid task number.");
                }
            } else if (input.startsWith("unmark ")) {
                try {
                    int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                    tasks.get(taskIndex).markAsNotDone();
                    System.out.println("_________________________________________");
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println(tasks.get(taskIndex));
                    System.out.println("_________________________________________");
                } catch (Exception e) {
                    System.out.println("Invalid task number.");
                }
            } else {
                Task newTask = new Task(input);
                tasks.add(newTask);
                System.out.println("_________________________________________");
                System.out.println("Added: " + newTask);
                System.out.println("_________________________________________");
            }
        }

        scanner.close();
    }
}
