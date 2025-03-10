import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// UNIVERSAL TASK CLASS
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

    public abstract String toFileFormat();

    @Override
    public String toString() {
        return getStatusIcon() + " " + description;
    }
}

// TASK SUBCLASSES
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
    protected LocalDate dueDate; // ✅ Store LocalDate instead of String

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

// CUSTOM EXCEPTION CLASS
class ViktorException extends Exception {
    public ViktorException(String message) {
        super(message);
    }
}

// UI CLASS (Handles User Interaction)
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
        System.out.println("Hi there. I'm Viktor.");
        System.out.println("How can I help you?");
        System.out.println("_________________________________________");
    }

    public void showExitMessage() {
        System.out.println("_________________________________________");
        System.out.println("Goodbye! Have a great day.");
        System.out.println("_________________________________________");
    }

    public void showError(String message) {
        System.out.println("Error: " + message);
    }

    public void showTaskList(TaskList taskList) {
        System.out.println("_________________________________________");
        System.out.println("Here are your tasks:");
        for (int i = 0; i < taskList.size(); i++) {
            System.out.println((i + 1) + ". " + taskList.get(i));
        }
        System.out.println("_________________________________________");
    }

    public void showFindResults(ArrayList<Task> matchingTasks) {
        System.out.println("_________________________________________");
        if (matchingTasks.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Here are the matching tasks in your list:");
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

// STORAGE CLASS (Handles File Operations)
class Storage {
    private String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public TaskList load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("No previous tasks found. Starting fresh!");
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
                        LocalDate date = LocalDate.parse(parts[3]); // ✅ Convert from String to LocalDate
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
            System.out.println("Warning: Corrupted data file. Starting fresh!");
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
                    throw new ViktorException("Your todo task is empty. Please try again.");
                }
                Task newTask = new ToDo(taskDescription);
                taskList.addTask(newTask);
                storage.save(taskList);
                System.out.println("Added: " + newTask);
            }
            else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split(" /by ");
                if (parts.length < 2) {
                    throw new ViktorException("Please specify a valid deadline format: deadline /by yyyy-MM-dd");
                }

                try {
                    LocalDate dueDate = LocalDate.parse(parts[1]);
                    Task newTask = new Deadline(parts[0].trim(), dueDate);
                    taskList.addTask(newTask);
                    storage.save(taskList);
                    System.out.println("Added: " + newTask);
                } catch (DateTimeParseException e) {
                    throw new ViktorException("Invalid date format! Please use yyyy-MM-dd.");
                }
            }

            else if (input.startsWith("find ")) {
                String keyword = input.substring(5).trim();
                if (keyword.isEmpty()) {
                    throw new ViktorException("Please enter a keyword to search.");
                }
                ArrayList<Task> matchingTasks = taskList.findTasks(keyword);
                ui.showFindResults(matchingTasks);
            }

            else {
                throw new ViktorException("Invalid command. Try again.");
            }
        } catch (ViktorException | IOException e) {
            ui.showError(e.getMessage());
        }
    }
}

// MAIN CLASS
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
            ui.showError("Error loading tasks. Starting fresh.");
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
