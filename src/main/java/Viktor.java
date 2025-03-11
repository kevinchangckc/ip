import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents a generic task with a description and completion status.
 */
abstract class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a new Task.
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks the task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the status icon of the task.
     * @return "[X]" if task is completed, "[ ]" otherwise.
     */
    public String getStatusIcon() {
        return (isDone ? "[X]" : "[ ]");
    }

    /**
     * Converts the task into a file-friendly format for storage.
     * @return Formatted string for storage.
     */
    public abstract String toFileFormat();

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}

/**
 * Represents a To-Do task.
 */
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

/**
 * Represents a deadline task with a due date.
 */
class Deadline extends Task {
    protected LocalDate dueDate; // ✅ Store LocalDate instead of String

    /**
     * Constructs a Deadline task with a description and due date.
     * @param description Description of the deadline task.
     * @param dueDate The due date in LocalDate format.
     */
    public Deadline(String description, LocalDate dueDate) {
        super(description);
        this.dueDate = dueDate;
    }

    @Override
    public String toFileFormat() {
        return "D | " + (isDone ? "1" : "0") + " | " + description + " | " + dueDate.toString(); // ✅ Save date as ISO format
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy"); // ✅ Format date
        return "[D]" + super.toString() + " (by: " + dueDate.format(formatter) + ")";
    }
}

/**
 * Represents an event task with a start and end time.
 */
class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Constructs an Event task with a description, start time, and end time.
     * @param description Description of the event.
     * @param from Start time of the event.
     * @param to End time of the event.
     */
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

/**
 * Custom exception class for chatbot errors.
 */
class ViktorException extends Exception {
    public ViktorException(String message) {
        super("⚠ " + message + " The pursuit of progress demands precision.");
    }
}

/**
 * Handles user interactions with the chatbot.
 */
class Ui {
    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showWelcomeMessage() {
        System.out.println("_________________________________________");
        System.out.println("Greetings. I am Viktor.");
        System.out.println("Progress must be forged, not waited upon.");
        // ASCII Art for "VIKTOR"
        System.out.println("         _   _      _                  ");
        System.out.println("__   __ (_) | | __ | |_    ___    _ __ ");
        System.out.println("\\ \\ / / | | | |/ / | __|  / _ \\  | '__|");
        System.out.println(" \\ V /  | | |   <  | |_  | (_) | | |   ");
        System.out.println("  \\_/   |_| |_|\\" + "_\\  \\__|  \\___/  |_|   ");
        System.out.println("_________________________________________");

        System.out.println("State your intent.");
        System.out.println("_________________________________________");
    }

    public void showExitMessage() {
        System.out.println("_________________________________________");
        System.out.println("There is still much work to be done.");
        System.out.println("Progress does not cease. Neither should you.");
        System.out.println("Farewell.");
        System.out.println("_________________________________________");
    }

    public void showError(String message) {
        System.out.println("⚠ ERROR: " + message);
        System.out.println("A flaw in the system... but flaws can be corrected.");
    }

    public void showTaskList(TaskList taskList) {
        System.out.println("_________________________________________");
        if (taskList.size() == 0) {
            System.out.println("Your list is empty. A void waiting to be filled.");
        } else {
            System.out.println("Task registry active:");
            for (int i = 0; i < taskList.size(); i++) {
                System.out.println((i + 1) + ". " + taskList.get(i));
            }
        }
        System.out.println("_________________________________________");
    }
    public void showFindResults(ArrayList<Task> matchingTasks) {
        System.out.println("_________________________________________");
        if (matchingTasks.isEmpty()) {
            System.out.println("Flawed input. No matches found. Refine your search.");
        } else {
            System.out.println("Pattern identified. These tasks align with your query:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + ". " + matchingTasks.get(i));
            }
        }
        System.out.println("_________________________________________");
    }
}

// TASKLIST CLASS (Handles Task Operations)
class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public Task removeTask(int index) {
        return tasks.remove(index);
    }

    public int size() {
        return tasks.size();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public ArrayList<Task> findTasks(String keyword) {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.description.toLowerCase().contains(keyword.toLowerCase())) {
                matchingTasks.add(task);
            }
        }
        return matchingTasks;
    }
}

/**
 * Stores and retrieves tasks from a file.
 */
class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public TaskList load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No prior data detected. A blank slate... full of potential.");
            return new TaskList(tasks);
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" \\| ");
                if (parts.length < 3) continue;

                String type = parts[0];
                boolean isDone = parts[1].equals("1");
                String description = parts[2];

                Task task;
                switch (type) {
                    case "T":
                        task = new ToDo(description);
                        break;
                    case "D":
                        LocalDate date = LocalDate.parse(parts[3]); //  Convert from String to LocalDate
                        task = new Deadline(description, date);
                        break;
                    case "E":
                        task = new Event(description, parts[3], parts[4]);
                        break;
                    default:
                        continue;
                }
                if (isDone) task.markAsDone();
                tasks.add(task);
            }
        } catch (Exception e) {
            System.out.println("⚠ The past is irrelevant. Only progress endures. Reconstructing from the beginning.");
            tasks.clear();
        }
        return new TaskList(tasks);
    }

    public void save(TaskList taskList) throws IOException {
        File file = new File(filePath);
        File directory = file.getParentFile();
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Task task : taskList.getTasks()) {
                writer.write(task.toFileFormat() + "\n");
            }
        }
    }
}

// PARSER CLASS (Handles Command Processing)
class Parser {
    private final Ui ui;
    private final TaskList taskList;
    private final Storage storage;

    public Parser(Ui ui, TaskList taskList, Storage storage) {
        this.ui = ui;
        this.taskList = taskList;
        this.storage = storage;
    }

    public void handleCommand(String input) {
        try {
            if (input.equalsIgnoreCase("bye")) {
                ui.showExitMessage();
                storage.save(taskList);
                System.exit(0);
            }
            else if (input.equalsIgnoreCase("list")) {
                ui.showTaskList(taskList);
            }
            else if (input.startsWith("todo ")) {
                String taskDescription = input.substring(5).trim();
                if (taskDescription.isEmpty()) {
                    throw new ViktorException("An empty task... devoid of purpose. " +
                            "Provide a task description.");
                }
                Task newTask = new ToDo(taskDescription);
                taskList.addTask(newTask);
                storage.save(taskList);
                System.out.println("Task acquired: " + newTask);
                System.out.println("Efficiency is paramount.");
            }
            else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split(" /by ");
                if (parts.length < 2) {
                    throw new ViktorException("Your input lacks order. Use the proper format: deadline /by yyyy-MM-dd.");
                }

                try {
                    LocalDate dueDate = LocalDate.parse(parts[1]);
                    Task newTask = new Deadline(parts[0].trim(), dueDate);
                    taskList.addTask(newTask);
                    storage.save(taskList);
                    System.out.println("Task acquired: " + newTask);
                    System.out.println("Efficiency is paramount.");
                } catch (DateTimeParseException e) {
                    throw new ViktorException("Time is the foundation of progress. " +
                            "Yet, you distort it. Use yyyy-MM-dd.");
                }
            }

            // Add an Event task
            else if (input.startsWith("event ")) {
                String[] parts = input.substring(6).split(" /from | /to ");
                if (parts.length == 3) {
                    Task newTask = new Event(parts[0], parts[1], parts[2]);
                    taskList.addTask(newTask);
                    System.out.println("_________________________________________");
                    System.out.println("Task acquired: " + newTask);
                    System.out.println("Efficiency is paramount.");
                    System.out.println("_________________________________________");
                } else {
                    System.out.println("Time is the foundation of progress. " +
                            "Yet, you distort it. Use: event <task> /from <start time> /to <end time>");
                }
            }

            else if (input.startsWith("find ")) {
                String keyword = input.substring(5).trim();
                if (keyword.isEmpty()) {
                    throw new ViktorException("Input required. Precision is key—state your query.");
                }
                ArrayList<Task> matchingTasks = taskList.findTasks(keyword);
                ui.showFindResults(matchingTasks);
            }

            // Mark a task as done
            else if (input.startsWith("done ")) {
                try {
                    int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                    taskList.get(taskIndex).markAsDone();
                    System.out.println("_________________________________________");
                    System.out.println("Progress achieved:");
                    System.out.println(taskList.get(taskIndex));
                    System.out.println("_________________________________________");
                } catch (Exception e) {
                    System.out.println("That task does not exist.Provide a valid number.");
                }
            }

            // Mark a task as not done
            else if (input.startsWith("unmark ")) {
                try {
                    int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;
                    taskList.get(taskIndex).markAsNotDone();
                    System.out.println("_________________________________________");
                    System.out.println("Task unmarked:");
                    System.out.println(taskList.get(taskIndex));
                    System.out.println("_________________________________________");
                } catch (Exception e) {
                    System.out.println("That task does not exist.Provide a valid number.");
                }
            }

            else if (input.startsWith("delete ")) {
                try {
                    int taskIndex = Integer.parseInt(input.split(" ")[1]) - 1;

                    if (taskIndex < 0 || taskIndex >= taskList.size()) {
                        throw new ViktorException("Flawed input. No such task exists. Refine your selection.");
                    }

                    Task removedTask = taskList.get(taskIndex);
                    taskList.removeTask(taskIndex);
                    storage.save(taskList);

                    System.out.println("_________________________________________");
                    System.out.println("Task purged: " + removedTask);
                    System.out.println("Only the essential remains.");
                    System.out.println("Now you have " + taskList.size() + " tasks in the list.");
                    System.out.println("_________________________________________");

                } catch (NumberFormatException e) {
                    throw new ViktorException("Miscalculation detected. Provide a valid numeric identifier.");
                } catch (IOException e) {
                    throw new ViktorException("Anomalous failure. Task removal was not persisted.");
                }
            }

            else {
                throw new ViktorException("Invalid input. Progress requires clarity. Recalibrate your command.");
            }
        } catch (ViktorException | IOException e) {
            ui.showError(e.getMessage());
        }
    }
}


/**
 * Main chatbot class that initializes components and handles user input.
 */
public class Viktor {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private Parser parser;

    public Viktor(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = storage.load();
        } catch (IOException e) {
            ui.showError("Data integrity compromised. Reconstruction is necessary.");
            tasks = new TaskList();
        }
        parser = new Parser(ui, tasks, storage);
    }

    public void run() {
        ui.showWelcomeMessage();
        while (true) {
            String input = ui.readCommand();
            parser.handleCommand(input);
        }
    }

    public static void main(String[] args) {
        new Viktor("data/viktor.txt").run();
    }}
