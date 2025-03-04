import java.util.Scanner;
import java.io.*;
import java.io.IOException;
import java.util.ArrayList;

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


// Level-5
class ViktorException extends Exception {
    public ViktorException(String message) {
        super(message);
    }
}

public class Viktor {
    private static final String FILE_PATH = "./data/viktor.txt"; // Ensure correct path
    private static Storage storage;
    private static ArrayList<Task> tasks;

    public static void main(String[] args) {
        storage = new Storage(FILE_PATH);

        // Load tasks from file
        try {
            tasks = storage.load();
        } catch (IOException e) {
            System.out.println("Error loading tasks from file. Starting fresh.");
            tasks = new ArrayList<>();
        }

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

                    try {
                        storage.save(tasks); // Save before exit
                    } catch (IOException e) {
                        System.out.println("Error: Unable to save tasks to file.");
                    }

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

                else if (input.startsWith("delete ")) {
                    String[] parts = input.split(" ");
                    if (parts.length != 2) {
                        throw new ViktorException("Oops! Please specify a valid task number to delete.");
                    }

                    try {
                        int taskIndex = Integer.parseInt(parts[1]) - 1;
                        if (taskIndex < 0 || taskIndex >= tasks.size()) {
                            throw new ViktorException("Oops! Task number does not exist.");
                        }

                        Task removedTask = tasks.remove(taskIndex);
                        System.out.println("_________________________________________");
                        System.out.println("Noted. I've removed this task:");
                        System.out.println(removedTask);
                        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                        System.out.println("_________________________________________");
                        try {
                            storage.save(tasks); // Save before exit
                        } catch (IOException e) {
                            System.out.println("Error: Unable to save tasks to file.");
                        }

                    } catch (NumberFormatException e) {
                        throw new ViktorException("Oops! Task number must be a valid number.");
                    }
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
                    try {
                        storage.save(tasks); // Save before exit
                    } catch (IOException e) {
                        System.out.println("Error: Unable to save tasks to file.");
                    }
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
                    try {
                        storage.save(tasks); // Save before exit
                    } catch (IOException e) {
                        System.out.println("Error: Unable to save tasks to file.");
                    }
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
                    try {
                        storage.save(tasks); // Save before exit
                    } catch (IOException e) {
                        System.out.println("Error: Unable to save tasks to file.");
                    }
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

