import java.util.Scanner;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

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

// universal Task class
abstract class Task {
    protected String description;
    protected boolean isDone;

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

    // 🔹 Abstract method that subclasses must implement
    public abstract String toFileFormat();

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}

/*
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

 */

// Level-4
class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String toFileFormat() {
        return "T | " + (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}

class Deadline extends Task {
    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toFileFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }
}

class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toFileFormat() {
        return "E | " + (isDone ? "1" : "0") + " | " + description + " | " + from + " | " + to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}

 /*
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

            // Exit program
            if (input.equalsIgnoreCase("bye")) {
                System.out.println("_________________________________________");
                System.out.println("Bye. Hope to see you again soon.");
                System.out.println("_________________________________________");
                break;
            }

            // List all tasks
            else if (input.equalsIgnoreCase("list")) {
                System.out.println("_________________________________________");
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < tasks.size(); i++) {
                    System.out.println((i + 1) + ". " + tasks.get(i));
                }
                System.out.println("_________________________________________");
            }

            // Mark a task as done
            else if (input.startsWith("mark ")) {
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
            }

            // Unmark a task
            else if (input.startsWith("unmark ")) {
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
            }

            // Add a ToDo task
            else if (input.startsWith("todo ")) {
                String taskDescription = input.substring(5);
                Task newTask = new ToDo(taskDescription);
                tasks.add(newTask);
                System.out.println("_________________________________________");
                System.out.println("Got it. I've added this task:");
                System.out.println(newTask);
                System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                System.out.println("_________________________________________");
            }

            // Add a Deadline task
            else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split(" /by ");
                if (parts.length == 2) {
                    Task newTask = new Deadline(parts[0], parts[1]);
                    tasks.add(newTask);
                    System.out.println("_________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println(newTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("_________________________________________");
                } else {
                    System.out.println("Invalid deadline format. Use: deadline <task> /by <date/time>");
                }
            }

            // Add an Event task
            else if (input.startsWith("event ")) {
                String[] parts = input.substring(6).split(" /from | /to ");
                if (parts.length == 3) {
                    Task newTask = new Event(parts[0], parts[1], parts[2]);
                    tasks.add(newTask);
                    System.out.println("_________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println(newTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("_________________________________________");
                } else {
                    System.out.println("Invalid event format. Use: event <task> /from <start time> /to <end time>");
                }
            }

            // Invalid command
            else {
                System.out.println("Sorry, I don't understand that command.");
            }
        }

        scanner.close();
    }
}
  */

// Level-5
class ViktorException extends Exception {
    public ViktorException(String message) {
        super(message);
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

            try {
                if (input.equalsIgnoreCase("bye")) {
                    System.out.println("_________________________________________");
                    System.out.println("Goodbye! Have a great day.");
                    System.out.println("_________________________________________");
                    break;
                }

                else if (input.equalsIgnoreCase("list")) {
                    System.out.println("_________________________________________");
                    System.out.println("Here are your tasks:");
                    for (int i = 0; i < tasks.size(); i++) {
                        System.out.println((i + 1) + ". " + tasks.get(i));
                    }
                    System.out.println("_________________________________________");
                }

                else if (input.startsWith("todo")) {
                    String taskDescription = input.substring(4).trim();
                    if (taskDescription.isEmpty()) {
                        throw new ViktorException("I'm afraid your todo task is empty. Please try again.");
                    }
                    Task newTask = new ToDo(taskDescription);
                    tasks.add(newTask);
                    System.out.println("_________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println(newTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("_________________________________________");
                }

                else if (input.startsWith("deadline")) {
                    String[] parts = input.substring(8).split(" /by ");
                    if (parts.length < 2) {
                        throw new ViktorException("I'm afraid you didn't include a deadline. Please specify a deadline using '/by'.");
                    }
                    Task newTask = new Deadline(parts[0], parts[1]);
                    tasks.add(newTask);
                    System.out.println("_________________________________________");
                    System.out.println("Got it. I've added this task:");
                    System.out.println(newTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("_________________________________________");
                }

                else if (input.startsWith("event")) {
                    String[] parts = input.substring(5).split(" /from | /to ");
                    if (parts.length < 3) {
                        throw new ViktorException("Please use '/from' and '/to' for event timing.");
                    }
                    Task newTask = new Event(parts[0], parts[1], parts[2]);
                    tasks.add(newTask);
                    System.out.println("_________________________________________");
                    System.out.println("Noted. I've added this task:");
                    System.out.println(newTask);
                    System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                    System.out.println("_________________________________________");
                }

                else {
                    throw new ViktorException("I'm afraid don't understand your command. Please try again.");
                }

            } catch (ViktorException e) {
                System.out.println("_________________________________________");
                System.out.println(e.getMessage());
                System.out.println("_________________________________________");
            }
        }

        scanner.close();
    }
}

// Level-7
 class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    // Load tasks from file
    public ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No previous tasks found. Starting fresh!");
            return tasks; // Return an empty task list
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" \\| ");
                if (parts.length < 3) continue; // Skip invalid lines

                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String description = parts[2];

                Task task;
                switch (type) {
                    case "T":
                        task = new ToDo(description);
                        break;
                    case "D":
                        task = new Deadline(description, parts[3]);
                        break;
                    case "E":
                        task = new Event(description, parts[3], parts[4]);
                        break;
                    default:
                        continue; // Skip unknown task types
                }
                if (isDone) task.markAsDone();
                tasks.add(task);
            }
        } catch (Exception e) {
            System.out.println("Warning: Corrupted data file. Starting fresh!");
            tasks.clear(); // Clear and start new list
        }
        return tasks;
    }

    // Save tasks to file
    public void save(ArrayList<Task> tasks) throws IOException {
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs(); // Create directories if they don’t exist
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + "\n");
            }
        }
    }
}

