import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

// Main Class
public class OnlineExamSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame();
        });
    }
}

// User Class
class User {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private List<ExamResult> examHistory;

    public User(String username, String password, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.examHistory = new ArrayList<>();
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<ExamResult> getExamHistory() { return examHistory; }
    public void addExamResult(ExamResult result) { examHistory.add(result); }
}

// ExamResult Class
class ExamResult {
    private String difficulty;
    private int totalQuestions;
    private int correctAnswers;
    private double percentage;
    private String date;

    public ExamResult(String difficulty, int totalQuestions, int correctAnswers, String date) {
        this.difficulty = difficulty;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.percentage = (correctAnswers * 100.0) / totalQuestions;
        this.date = date;
    }

    public ExamResult(String difficulty, int totalQuestions, int correctAnswers) {
        this(difficulty, totalQuestions, correctAnswers,
             LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }

    public String getDifficulty() { return difficulty; }
    public int getTotalQuestions() { return totalQuestions; }
    public int getCorrectAnswers() { return correctAnswers; }
    public double getPercentage() { return percentage; }
    public String getDate() { return date; }

    @Override
    public String toString() {
        return String.format("%s | %s | Score: %d/%d (%.2f%%)",
            date, difficulty, correctAnswers, totalQuestions, percentage);
    }
}

// Question Class
class Question {
    private String question;
    private String[] options;
    private int correctAnswer;

    public Question(String question, String[] options, int correctAnswer) {
        this.question = question;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public String getQuestion() { return question; }
    public String[] getOptions() { return options; }
    public int getCorrectAnswer() { return correctAnswer; }
}

// QuestionBank Class
class QuestionBank {
    private static final List<Question> EASY_QUESTIONS = Arrays.asList(
        new Question("What is the size of int in Java?",
            new String[]{"8 bits", "16 bits", "32 bits", "64 bits"}, 2),
        new Question("Which keyword is used to create a class in Java?",
            new String[]{"class", "Class", "CLASS", "define"}, 0),
        new Question("What is the default value of boolean in Java?",
            new String[]{"true", "false", "0", "null"}, 1),
        new Question("Which method is the entry point of a Java program?",
            new String[]{"start()", "main()", "run()", "init()"}, 1),
        new Question("What does JVM stand for?",
            new String[]{"Java Virtual Machine", "Java Variable Method", "Java Visual Machine", "Java Verified Method"}, 0),
        new Question("Which data type is used to store a single character in Java?",
            new String[]{"String", "char", "Character", "byte"}, 1),
        new Question("What is the extension of Java source file?",
            new String[]{".class", ".java", ".jar", ".exe"}, 1),
        new Question("Which operator is used to compare two values?",
            new String[]{"=", "==", "===", "!="}, 1),
        new Question("What is used to get input from console in Java?",
            new String[]{"Scanner", "Reader", "Input", "Console"}, 0),
        new Question("Which package contains Scanner class?",
            new String[]{"java.io", "java.util", "java.lang", "java.scanner"}, 1),
        new Question("What is the superclass of all classes in Java?",
            new String[]{"System", "String", "Object", "Class"}, 2),
        new Question("Which keyword is used for inheritance in Java?",
            new String[]{"inherits", "extends", "implements", "super"}, 1),
        new Question("What is the default value of an int variable?",
            new String[]{"0", "null", "-1", "undefined"}, 0),
        new Question("Which loop is guaranteed to execute at least once?",
            new String[]{"for", "while", "do-while", "foreach"}, 2),
        new Question("What is used to terminate a loop in Java?",
            new String[]{"exit", "stop", "break", "end"}, 2)
    );

    private static final List<Question> MEDIUM_QUESTIONS = Arrays.asList(
        new Question("Which collection class allows duplicate elements and maintains insertion order?",
            new String[]{"HashSet", "TreeSet", "ArrayList", "HashMap"}, 2),
        new Question("What is the purpose of the 'final' keyword when applied to a class?",
            new String[]{"Makes it abstract", "Prevents inheritance", "Allows multiple instances", "Makes it static"}, 1),
        new Question("Which exception is thrown when dividing by zero?",
            new String[]{"NullPointerException", "ArithmeticException", "NumberFormatException", "IllegalArgumentException"}, 1),
        new Question("What is method overloading?",
            new String[]{"Same method name, different parameters", "Different method name, same parameters", "Redefining parent method", "Using multiple classes"}, 0),
        new Question("Which interface must be implemented to create a thread by implementing an interface?",
            new String[]{"Thread", "Runnable", "Callable", "Executor"}, 1),
        new Question("What is the difference between == and .equals()?",
            new String[]{"No difference", "== compares references, .equals() compares content", "== is faster", ".equals() is deprecated"}, 1),
        new Question("Which keyword is used to handle exceptions?",
            new String[]{"try-catch", "throw-catch", "handle-error", "exception-catch"}, 0),
        new Question("What is autoboxing in Java?",
            new String[]{"Automatic conversion of primitive to wrapper class", "Creating boxes automatically", "Boxing algorithm", "Automated testing"}, 0),
        new Question("Which collection does not allow null values?",
            new String[]{"ArrayList", "HashMap", "TreeMap", "LinkedList"}, 2),
        new Question("What is the purpose of the 'static' keyword?",
            new String[]{"Makes variable constant", "Belongs to class, not instance", "Prevents changes", "Allows inheritance"}, 1),
        new Question("Which method is called when an object is garbage collected?",
            new String[]{"finalize()", "delete()", "cleanup()", "dispose()"}, 0),
        new Question("What is polymorphism in Java?",
            new String[]{"Multiple forms", "Multiple classes", "Multiple methods", "Multiple variables"}, 0),
        new Question("Which access modifier allows access within the same package?",
            new String[]{"private", "protected", "default", "public"}, 2),
        new Question("What is the purpose of 'super' keyword?",
            new String[]{"Call parent class constructor/methods", "Create superclass", "Make class superior", "Override methods"}, 0),
        new Question("Which interface is the root of collection hierarchy?",
            new String[]{"List", "Set", "Collection", "Map"}, 2),
        new Question("What is the difference between abstract class and interface?",
            new String[]{"No difference", "Abstract class can have concrete methods", "Interface is faster", "Abstract class is deprecated"}, 1),
        new Question("Which method is used to start a thread?",
            new String[]{"run()", "start()", "begin()", "execute()"}, 1),
        new Question("What is synchronization in Java?",
            new String[]{"Time management", "Thread-safe access to shared resources", "Data backup", "Code optimization"}, 1),
        new Question("Which keyword is used to prevent method overriding?",
            new String[]{"static", "final", "abstract", "private"}, 1),
        new Question("What is the purpose of 'this' keyword?",
            new String[]{"Refers to current object", "Refers to parent class", "Creates new object", "Deletes object"}, 0),
        new Question("Which exception is checked exception?",
            new String[]{"NullPointerException", "IOException", "ArithmeticException", "ArrayIndexOutOfBoundsException"}, 1),
        new Question("What is constructor chaining?",
            new String[]{"Calling one constructor from another", "Creating chain of objects", "Linking classes", "Method linking"}, 0),
        new Question("Which collection is synchronized?",
            new String[]{"ArrayList", "Vector", "HashMap", "HashSet"}, 1),
        new Question("What is the default size of ArrayList?",
            new String[]{"5", "10", "16", "32"}, 1),
        new Question("Which method compares two strings ignoring case?",
            new String[]{"equals()", "equalsIgnoreCase()", "compare()", "compareTo()"}, 1),
        new Question("What is lambda expression in Java?",
            new String[]{"Anonymous function", "Special loop", "Error handling", "Data structure"}, 0),
        new Question("Which keyword is used to define constants?",
            new String[]{"const", "final", "static", "constant"}, 1),
        new Question("What is the purpose of StringBuilder?",
            new String[]{"Build strings efficiently", "Create buildings", "String comparison", "String deletion"}, 0),
        new Question("Which method is used to convert String to int?",
            new String[]{"parseInt()", "toInt()", "convertInt()", "getInt()"}, 0),
        new Question("What is encapsulation in Java?",
            new String[]{"Wrapping data and methods in a class", "Creating capsules", "Data hiding only", "Method hiding"}, 0)
    );

    private static final List<Question> HARD_QUESTIONS = Arrays.asList(
        new Question("What is the time complexity of HashMap get() operation?",
            new String[]{"O(1)", "O(log n)", "O(n)", "O(n log n)"}, 0),
        new Question("Which design pattern ensures a class has only one instance?",
            new String[]{"Factory", "Singleton", "Observer", "Builder"}, 1),
        new Question("What is the difference between Callable and Runnable?",
            new String[]{"No difference", "Callable can return value and throw exception", "Callable is faster", "Runnable is deprecated"}, 1),
        new Question("What is memory leak in Java?",
            new String[]{"Physical memory damage", "Objects not garbage collected despite being unused", "Disk space issue", "CPU overuse"}, 1),
        new Question("Which concurrent collection provides thread-safe operations?",
            new String[]{"ArrayList", "HashMap", "ConcurrentHashMap", "TreeMap"}, 2),
        new Question("What is the purpose of volatile keyword?",
            new String[]{"Makes variable constant", "Ensures visibility across threads", "Prevents inheritance", "Speeds up access"}, 1),
        new Question("What is double-checked locking?",
            new String[]{"Checking lock twice", "Singleton pattern optimization", "Security feature", "Error handling"}, 1),
        new Question("Which garbage collector is default in Java 11+?",
            new String[]{"Serial GC", "Parallel GC", "G1 GC", "ZGC"}, 2),
        new Question("What is the difference between Heap and Stack memory?",
            new String[]{"No difference", "Heap stores objects, Stack stores method calls and local variables", "Heap is faster", "Stack is larger"}, 1),
        new Question("What is covariant return type?",
            new String[]{"Returning same type", "Overriding method can return subtype", "Multiple returns", "Error condition"}, 1),
        new Question("Which annotation is used for dependency injection in Spring?",
            new String[]{"@Inject", "@Autowired", "@Component", "@Bean"}, 1),
        new Question("What is the diamond problem in inheritance?",
            new String[]{"Shape issue", "Multiple inheritance ambiguity", "Syntax error", "Diamond operator"}, 1),
        new Question("Which method is used for deep cloning?",
            new String[]{"clone()", "deepClone()", "Serialization", "copy()"}, 2),
        new Question("What is the purpose of CompletableFuture?",
            new String[]{"Complete tasks", "Asynchronous programming", "Future predictions", "Task scheduling"}, 1),
        new Question("Which SOLID principle states 'classes should be open for extension but closed for modification'?",
            new String[]{"Single Responsibility", "Open/Closed", "Liskov Substitution", "Interface Segregation"}, 1),
        new Question("What is the difference between fail-fast and fail-safe iterators?",
            new String[]{"Speed difference", "Fail-fast throws ConcurrentModificationException, fail-safe doesn't", "No difference", "Error types"}, 1),
        new Question("What is phantom reference in Java?",
            new String[]{"Null reference", "Reference for post-finalization cleanup", "Ghost object", "Weak reference"}, 1),
        new Question("Which method ordering provides happens-before guarantee?",
            new String[]{"No guarantee", "synchronized, volatile, final", "All methods", "Only static"}, 1),
        new Question("What is the purpose of ForkJoinPool?",
            new String[]{"Database pooling", "Divide-and-conquer parallel processing", "Connection pooling", "Thread pooling"}, 1),
        new Question("Which functional interface represents a supplier?",
            new String[]{"Function", "Consumer", "Supplier", "Predicate"}, 2),
        new Question("What is method reference in Java 8?",
            new String[]{"Reference to method", "Shorthand for lambda expression", "Documentation", "Pointer"}, 1),
        new Question("Which stream operation is terminal?",
            new String[]{"map()", "filter()", "collect()", "peek()"}, 2),
        new Question("What is the difference between ClassNotFoundException and NoClassDefFoundError?",
            new String[]{"No difference", "Checked vs unchecked, compile vs runtime", "Same exception", "Deprecated vs new"}, 1),
        new Question("Which annotation marks a method as transactional in Spring?",
            new String[]{"@Transaction", "@Transactional", "@Tx", "@Atomic"}, 1),
        new Question("What is the purpose of @FunctionalInterface annotation?",
            new String[]{"Makes class functional", "Ensures interface has exactly one abstract method", "Improves performance", "Required for lambdas"}, 1),
        new Question("What is the difference between CountDownLatch and CyclicBarrier?",
            new String[]{"No difference", "CountDownLatch is one-time use, CyclicBarrier is reusable", "Speed difference", "Thread count"}, 1),
        new Question("Which class is used for atomic operations on integers?",
            new String[]{"Integer", "AtomicInteger", "SynchronizedInteger", "ThreadSafeInteger"}, 1),
        new Question("What is the purpose of @SpringBootApplication annotation?",
            new String[]{"Starts Spring Boot", "Combines @Configuration, @EnableAutoConfiguration, @ComponentScan", "Creates beans", "Enables web"}, 1),
        new Question("Which HTTP method is idempotent?",
            new String[]{"POST", "GET", "PATCH", "All methods"}, 1),
        new Question("What is reactive programming in Spring?",
            new String[]{"Quick response", "Non-blocking asynchronous programming", "Active programming", "Fast processing"}, 1),
        new Question("Which design pattern is used in Java I/O streams?",
            new String[]{"Singleton", "Decorator", "Factory", "Observer"}, 1),
        new Question("What is the purpose of @Cacheable annotation?",
            new String[]{"Creates cache", "Enables method result caching", "Clears cache", "Validates cache"}, 1),
        new Question("Which collection has O(1) add/remove at both ends?",
            new String[]{"ArrayList", "LinkedList", "ArrayDeque", "Vector"}, 2),
        new Question("What is the difference between @Component and @Bean?",
            new String[]{"No difference", "@Component is class-level, @Bean is method-level", "@Bean is deprecated", "Same annotation"}, 1),
        new Question("Which lock provides fairness policy?",
            new String[]{"synchronized", "ReentrantLock", "ReadWriteLock", "No lock"}, 1),
        new Question("What is the purpose of Optional class?",
            new String[]{"Make things optional", "Avoid NullPointerException", "Improve performance", "Data structure"}, 1),
        new Question("Which annotation is used for validation in Spring?",
            new String[]{"@Valid", "@Validate", "@Check", "@Verify"}, 0),
        new Question("What is the difference between Executor and ExecutorService?",
            new String[]{"No difference", "ExecutorService extends Executor with lifecycle management", "Speed difference", "Thread count"}, 1),
        new Question("Which algorithm does TreeMap use internally?",
            new String[]{"Hash table", "Red-Black tree", "Binary tree", "AVL tree"}, 1),
        new Question("What is the purpose of @Async annotation?",
            new String[]{"Make method synchronous", "Execute method asynchronously", "Speed up method", "Cache method"}, 1),
        new Question("Which class represents an immutable date-time?",
            new String[]{"Date", "LocalDateTime", "Calendar", "Timestamp"}, 1),
        new Question("What is the difference between interrupt() and stop()?",
            new String[]{"No difference", "interrupt() is safe, stop() is deprecated", "Same method", "Speed difference"}, 1),
        new Question("Which pattern is used in Stream API?",
            new String[]{"Singleton", "Builder", "Factory", "Observer"}, 1),
        new Question("What is the purpose of @Profile annotation?",
            new String[]{"User profile", "Environment-specific bean creation", "Performance profiling", "Security profile"}, 1),
        new Question("Which interface represents a predicate in Java 8?",
            new String[]{"Function", "Consumer", "Predicate", "Supplier"}, 2),
        new Question("What is the difference between HashMap and ConcurrentHashMap?",
            new String[]{"No difference", "ConcurrentHashMap is thread-safe", "HashMap is faster", "ConcurrentHashMap is deprecated"}, 1),
        new Question("Which annotation enables scheduling in Spring?",
            new String[]{"@Schedule", "@EnableScheduling", "@Scheduled", "@Timer"}, 1),
        new Question("What is the purpose of SoftReference?",
            new String[]{"Soft delete", "Memory-sensitive caching", "Weak reference", "Strong reference"}, 1),
        new Question("Which HTTP status code indicates resource created?",
            new String[]{"200", "201", "204", "301"}, 1),
        new Question("What is the difference between @RequestMapping and @GetMapping?",
            new String[]{"No difference", "@GetMapping is shortcut for GET method", "@RequestMapping is deprecated", "Same annotation"}, 1)
    );

    public static List<Question> getQuestions(String difficulty, int count) {
        List<Question> pool;
        switch (difficulty.toLowerCase()) {
            case "easy":
                pool = new ArrayList<>(EASY_QUESTIONS);
                break;
            case "medium":
                pool = new ArrayList<>(MEDIUM_QUESTIONS);
                break;
            case "hard":
                pool = new ArrayList<>(HARD_QUESTIONS);
                break;
            default:
                pool = new ArrayList<>(EASY_QUESTIONS);
        }

        Collections.shuffle(pool);
        return pool.subList(0, Math.min(count, pool.size()));
    }
}

// UserManager Class
class UserManager {
    private static final String DATA_FILE = "exam_users.txt";
    private static Map<String, User> users = new HashMap<>();

    static {
        loadUsers();
    }

    private static void loadUsers() {
        File file = new File(DATA_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("USER:")) {
                    String[] parts = line.substring(5).split("\\|");
                    if (parts.length >= 4) {
                        User user = new User(parts[0], parts[1], parts[2], parts[3]);

                        String historyLine = br.readLine();
                        if (historyLine != null && historyLine.startsWith("HISTORY:")) {
                            String historyData = historyLine.substring(8);
                            if (!historyData.isEmpty()) {
                                String[] results = historyData.split(";");
                                for (String result : results) {
                                    String[] resultParts = result.split(",");
                                    if (resultParts.length >= 4) {
                                        ExamResult examResult = new ExamResult(
                                            resultParts[0],
                                            Integer.parseInt(resultParts[1]),
                                            Integer.parseInt(resultParts[2]),
                                            resultParts[3]
                                        );
                                        user.addExamResult(examResult);
                                    }
                                }
                            }
                        }

                        users.put(user.getUsername(), user);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveUsers() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (User user : users.values()) {
                bw.write("USER:" + user.getUsername() + "|" + user.getPassword() + "|" +
                        user.getFullName() + "|" + user.getEmail());
                bw.newLine();

                StringBuilder history = new StringBuilder("HISTORY:");
                for (int i = 0; i < user.getExamHistory().size(); i++) {
                    ExamResult result = user.getExamHistory().get(i);
                    if (i > 0) history.append(";");
                    history.append(result.getDifficulty()).append(",")
                           .append(result.getTotalQuestions()).append(",")
                           .append(result.getCorrectAnswers()).append(",")
                           .append(result.getDate());
                }
                bw.write(history.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    public static boolean registerUser(String username, String password, String fullName, String email) {
        if (users.containsKey(username)) {
            return false;
        }
        User user = new User(username, password, fullName, email);
        users.put(username, user);
        saveUsers();
        return true;
    }

    public static void updateUser(User user) {
        users.put(user.getUsername(), user);
        saveUsers();
    }
}

// LoginFrame Class
class LoginFrame extends JFrame {

    public LoginFrame() {
        setTitle("Online Examination System - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Online Examination System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 50));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton loginButton = new JButton("Login");
        loginButton.setFocusPainted(false);
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setPreferredSize(new Dimension(150, 50));
        loginButton.addActionListener(e -> showLoginDialog());

        JButton registerButton = new JButton("Register");
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 16));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.setPreferredSize(new Dimension(150, 50));
        registerButton.addActionListener(e -> showRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private void showLoginDialog() {
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Login", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = UserManager.authenticate(username, password);
            if (user != null) {
                dispose();
                new DashboardFrame(user);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showRegister() {
        new RegisterFrame(this);
    }
}

// RegisterFrame Class
class RegisterFrame extends JDialog {
    private JTextField usernameField, fullNameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;

    public RegisterFrame(JFrame parent) {
        super(parent, "Register New User", true);
        setSize(450, 400);
        setLocationRelativeTo(parent);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(20);
        mainPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        mainPanel.add(fullNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        mainPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        mainPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        mainPanel.add(confirmPasswordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(Color.GREEN);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerButton.addActionListener(e -> register());

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.RED);
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setFocusPainted(false);
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private void register() {
        String username = usernameField.getText().trim();
        String fullName = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (UserManager.registerUser(username, password, fullName, email)) {
            JOptionPane.showMessageDialog(this, "Registration successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

// DashboardFrame Class
class DashboardFrame extends JFrame {
    private User currentUser;

    public DashboardFrame(User user) {
        this.currentUser = user;
        setTitle("Dashboard - Welcome " + user.getFullName());
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 248, 255));

        JLabel titleLabel = new JLabel("Welcome, " + user.getFullName() + "!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 10, 15));
        buttonPanel.setBackground(new Color(240, 248, 255));

        JButton startExamButton = createStyledButton("Start Exam", Color.BLUE);
        startExamButton.addActionListener(e -> selectDifficulty());

        JButton viewHistoryButton = createStyledButton("View Exam History", Color.MAGENTA);
        viewHistoryButton.addActionListener(e -> viewHistory());

        JButton updateProfileButton = createStyledButton("Update Profile", Color.PINK);
        updateProfileButton.addActionListener(e -> updateProfile());

        JButton changePasswordButton = createStyledButton("Change Password", Color.ORANGE);
        changePasswordButton.addActionListener(e -> changePassword());

        JButton logoutButton = createStyledButton("Logout", Color.RED);
        logoutButton.addActionListener(e -> logout());

        buttonPanel.add(startExamButton);
        buttonPanel.add(viewHistoryButton);
        buttonPanel.add(updateProfileButton);
        buttonPanel.add(changePasswordButton);
        buttonPanel.add(logoutButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void selectDifficulty() {
        String[] difficulties = {"Easy (10 questions)", "Medium (25 questions)", "Hard (50 questions)", "Hard (100 questions)"};
        String choice = (String) JOptionPane.showInputDialog(this, "Select Difficulty Level:",
            "Start Exam", JOptionPane.QUESTION_MESSAGE, null, difficulties, difficulties[0]);

        if (choice != null) {
            String difficulty;
            int questionCount;

            if (choice.startsWith("Easy")) {
                difficulty = "Easy";
                questionCount = 10;
            } else if (choice.startsWith("Medium")) {
                difficulty = "Medium";
                questionCount = 25;
            } else if (choice.contains("50")) {
                difficulty = "Hard";
                questionCount = 50;
            } else {
                difficulty = "Hard";
                questionCount = 100;
            }

            dispose();
            new ExamFrame(currentUser, difficulty, questionCount);
        }
    }

    private void viewHistory() {
        if (currentUser.getExamHistory().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No exam history available.", "History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder history = new StringBuilder("<html><body style='width: 400px'>");
        history.append("<h2>Exam History</h2>");
        history.append("<table border='1' cellpadding='5'>");
        history.append("<tr><th>Date</th><th>Level</th><th>Score</th><th>%</th></tr>");

        for (ExamResult result : currentUser.getExamHistory()) {
            history.append("<tr>");
            history.append("<td>").append(result.getDate()).append("</td>");
            history.append("<td>").append(result.getDifficulty()).append("</td>");
            history.append("<td>").append(result.getCorrectAnswers()).append("/").append(result.getTotalQuestions()).append("</td>");
            history.append("<td>").append(String.format("%.2f", result.getPercentage())).append("%</td>");
            history.append("</tr>");
        }

        history.append("</table></body></html>");

        JOptionPane.showMessageDialog(this, history.toString(), "Exam History", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateProfile() {
        JTextField fullNameField = new JTextField(currentUser.getFullName());
        JTextField emailField = new JTextField(currentUser.getEmail());

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Full Name:"));
        panel.add(fullNameField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Profile", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String newFullName = fullNameField.getText().trim();
            String newEmail = emailField.getText().trim();

            if (!newFullName.isEmpty() && !newEmail.isEmpty()) {
                currentUser.setFullName(newFullName);
                currentUser.setEmail(newEmail);
                UserManager.updateUser(currentUser);
                JOptionPane.showMessageDialog(this, "Profile updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                setTitle("Dashboard - Welcome " + currentUser.getFullName());
            }
        }
    }

    private void changePassword() {
        JPasswordField currentPasswordField = new JPasswordField();
        JPasswordField newPasswordField = new JPasswordField();
        JPasswordField confirmPasswordField = new JPasswordField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Current Password:"));
        panel.add(currentPasswordField);
        panel.add(new JLabel("New Password:"));
        panel.add(newPasswordField);
        panel.add(new JLabel("Confirm Password:"));
        panel.add(confirmPasswordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change Password", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String currentPassword = new String(currentPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (!currentPassword.equals(currentUser.getPassword())) {
                JOptionPane.showMessageDialog(this, "Current password is incorrect!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "New password cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            currentUser.setPassword(newPassword);
            UserManager.updateUser(currentUser);
            JOptionPane.showMessageDialog(this, "Password changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?",
            "Logout", JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
}

// ExamFrame Class
class ExamFrame extends JFrame {
    private User currentUser;
    private String difficulty;
    private List<Question> questions;
    private int[] userAnswers;
    private int currentQuestionIndex = 0;
    private javax.swing.Timer timer;
    private int timeRemaining;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup optionGroup;
    private JLabel timerLabel;
    private JLabel questionNumberLabel;
    private JButton prevButton, nextButton, submitButton;

    public ExamFrame(User user, String difficulty, int questionCount) {
        this.currentUser = user;
        this.difficulty = difficulty;
        this.questions = QuestionBank.getQuestions(difficulty, questionCount);
        this.userAnswers = new int[questions.size()];

        for (int i = 0; i < userAnswers.length; i++) {
            userAnswers[i] = -1;
        }

        setTitle("Exam - " + difficulty + " Level (" + questionCount + " Questions)");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                JOptionPane.showMessageDialog(ExamFrame.this,
                    "Please submit the exam before closing!", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        initUI();
        startTimer();
        displayQuestion();

        setVisible(true);
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        questionNumberLabel = new JLabel("Question 1 of " + questions.size());
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionNumberLabel.setForeground(Color.WHITE);

        timerLabel = new JLabel("Time: 10:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE);

        headerPanel.add(questionNumberLabel, BorderLayout.WEST);
        headerPanel.add(timerLabel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Question Panel
        JPanel questionPanel = new JPanel(new BorderLayout(10, 10));
        questionPanel.setBackground(Color.WHITE);
        questionPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204), 2),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        questionPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        optionsPanel.setBackground(Color.WHITE);
        optionsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        optionGroup = new ButtonGroup();
        optionButtons = new JRadioButton[4];

        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 14));
            optionButtons[i].setBackground(Color.WHITE);
            final int index = i;
            optionButtons[i].addActionListener(e -> userAnswers[currentQuestionIndex] = index);
            optionGroup.add(optionButtons[i]);
            optionsPanel.add(optionButtons[i]);
        }

        questionPanel.add(optionsPanel, BorderLayout.CENTER);
        mainPanel.add(questionPanel, BorderLayout.CENTER);

        // Navigation Panel
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        navPanel.setBackground(Color.WHITE);

        prevButton = new JButton("← Previous");
        prevButton.setFont(new Font("Arial", Font.BOLD, 14));
        prevButton.setBackground(Color.GRAY);
        prevButton.setForeground(Color.WHITE);
        prevButton.setFocusPainted(false);
        prevButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        prevButton.addActionListener(e -> previousQuestion());

        nextButton = new JButton("Next →");
        nextButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextButton.setBackground(Color.BLUE);
        nextButton.setForeground(Color.WHITE);
        nextButton.setFocusPainted(false);
        nextButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        nextButton.addActionListener(e -> nextQuestion());

        submitButton = new JButton("Submit Exam");
        submitButton.setFont(new Font("Arial", Font.BOLD, 14));
        submitButton.setBackground(Color.GREEN);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        submitButton.addActionListener(e -> submitExam());

        navPanel.add(prevButton);
        navPanel.add(nextButton);
        navPanel.add(submitButton);

        mainPanel.add(navPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void startTimer() {
        timeRemaining = questions.size() * 60; // 1 minute per question

        timer = new javax.swing.Timer(1000, e -> {
            timeRemaining--;
            updateTimerDisplay();

            if (timeRemaining <= 60) {
                timerLabel.setForeground(Color.RED);
            }

            if (timeRemaining <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this, "Time's up! Exam will be submitted automatically.",
                    "Time Up", JOptionPane.WARNING_MESSAGE);
                submitExam();
            }
        });

        timer.start();
    }

    private void updateTimerDisplay() {
        int minutes = timeRemaining / 60;
        int seconds = timeRemaining % 60;
        timerLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    private void displayQuestion() {
        Question q = questions.get(currentQuestionIndex);
        questionLabel.setText("<html><body style='width: 700px'><b>Q" + (currentQuestionIndex + 1) +
            ".</b> " + q.getQuestion() + "</body></html>");

        String[] options = q.getOptions();
        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText((char)('A' + i) + ". " + options[i]);
        }

        optionGroup.clearSelection();
        if (userAnswers[currentQuestionIndex] != -1) {
            optionButtons[userAnswers[currentQuestionIndex]].setSelected(true);
        }

        questionNumberLabel.setText("Question " + (currentQuestionIndex + 1) + " of " + questions.size());
        prevButton.setEnabled(currentQuestionIndex > 0);
        nextButton.setEnabled(currentQuestionIndex < questions.size() - 1);
    }

    private void previousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
            displayQuestion();
        }
    }

    private void nextQuestion() {
        if (currentQuestionIndex < questions.size() - 1) {
            currentQuestionIndex++;
            displayQuestion();
        }
    }

    private void submitExam() {
        int unanswered = 0;
        for (int answer : userAnswers) {
            if (answer == -1) unanswered++;
        }

        if (unanswered > 0) {
            int result = JOptionPane.showConfirmDialog(this,
                "You have " + unanswered + " unanswered questions. Submit anyway?",
                "Confirm Submit", JOptionPane.YES_NO_OPTION);

            if (result != JOptionPane.YES_OPTION) {
                return;
            }
        }

        timer.stop();

        int correctAnswers = 0;
        for (int i = 0; i < questions.size(); i++) {
            if (userAnswers[i] == questions.get(i).getCorrectAnswer()) {
                correctAnswers++;
            }
        }

        ExamResult examResult = new ExamResult(difficulty, questions.size(), correctAnswers);
        currentUser.addExamResult(examResult);
        UserManager.updateUser(currentUser);

        showResults(correctAnswers);
    }

    private void showResults(int correctAnswers) {
        double percentage = (correctAnswers * 100.0) / questions.size();

        String message = String.format(
            "<html><body style='width: 300px; text-align: center'>" +
            "<h2>Exam Completed!</h2>" +
            "<p><b>Total Questions:</b> %d</p>" +
            "<p><b>Correct Answers:</b> %d</p>" +
            "<p><b>Wrong Answers:</b> %d</p>" +
            "<p><b>Percentage:</b> %.2f%%</p>" +
            "<h3 style='color: %s'>%s</h3>" +
            "</body></html>",
            questions.size(), correctAnswers, questions.size() - correctAnswers, percentage,
            percentage >= 70 ? "green" : "red",
            percentage >= 70 ? "PASSED" : "FAILED"
        );

        JOptionPane.showMessageDialog(this, message, "Exam Results", JOptionPane.INFORMATION_MESSAGE);

        dispose();
        new DashboardFrame(currentUser);
    }
}