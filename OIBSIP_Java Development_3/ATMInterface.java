import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;

// Account class to store user account information
class Account {
    private String userId;
    private String pin;
    private String name;
    private double balance;
    private ArrayList<String> transactionHistory;
    
    public Account(String userId, String pin, String name, double initialBalance) {
        this.userId = userId;
        this.pin = pin;
        this.name = name;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        addTransaction("Account created with initial balance: $" + initialBalance);
    }
    
    public Account(JSONObject json) {
        this.userId = json.getString("userId");
        this.pin = json.getString("pin");
        this.name = json.getString("name");
        this.balance = json.getDouble("balance");
        this.transactionHistory = new ArrayList<>();
        JSONArray history = json.getJSONArray("transactionHistory");
        for (int i = 0; i < history.length(); i++) {
            this.transactionHistory.add(history.getString(i));
        }
    }
    
    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        json.put("userId", userId);
        json.put("pin", pin);
        json.put("name", name);
        json.put("balance", balance);
        json.put("transactionHistory", new JSONArray(transactionHistory));
        return json;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public String getPin() {
        return pin;
    }
    
    public String getName() {
        return name;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void deposit(double amount) {
        balance += amount;
        addTransaction("Deposit: +$" + amount + " | Balance: $" + balance);
    }
    
    public boolean withdraw(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        addTransaction("Withdrawal: -$" + amount + " | Balance: $" + balance);
        return true;
    }
    
    public boolean transfer(Account recipient, double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        recipient.deposit(amount);
        addTransaction("Transfer: -$" + amount + " to " + recipient.getName() + " | Balance: $" + balance);
        return true;
    }
    
    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }
    
    private void addTransaction(String transaction) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        transactionHistory.add(timestamp + " - " + transaction);
    }
}

// ATM Manager class to handle all accounts
class ATMManager {
    private HashMap<String, Account> accounts;
    private Random random;
    private static final String DATA_FILE = "atm_data.json";
    private static final String ADMIN_PASSWORD = "admin123"; // Admin password
    
    public ATMManager() {
        random = new Random();
        loadData();
    }
    
    public String createAccount(String name, double initialBalance) {
        String userId = generateUserId();
        String pin = generatePin();
        Account account = new Account(userId, pin, name, initialBalance);
        accounts.put(userId, account);
        saveData();
        return "Account Created!\nUser ID: " + userId + "\nPIN: " + pin + "\nPlease save these credentials.";
    }
    
    private String generateUserId() {
        String userId;
        do {
            userId = "USER" + (1000 + random.nextInt(9000));
        } while (accounts.containsKey(userId));
        return userId;
    }
    
    private String generatePin() {
        return String.format("%04d", random.nextInt(10000));
    }
    
    public Account authenticate(String userId, String pin) {
        Account account = accounts.get(userId);
        if (account != null && account.getPin().equals(pin)) {
            return account;
        }
        return null;
    }
    
    public boolean authenticateAdmin(String password) {
        return ADMIN_PASSWORD.equals(password);
    }
    
    public Account getAccountByUserId(String userId) {
        return accounts.get(userId);
    }
    
    public ArrayList<String> getAllUserIds() {
        return new ArrayList<>(accounts.keySet());
    }
    
    public HashMap<String, Account> getAllAccounts() {
        return accounts;
    }
    
    public void saveData() {
        try {
            JSONObject mainObj = new JSONObject();
            JSONArray accountsArray = new JSONArray();
            
            for (Account account : accounts.values()) {
                accountsArray.put(account.toJSON());
            }
            
            mainObj.put("accounts", accountsArray);
            mainObj.put("lastUpdated", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            
            FileWriter file = new FileWriter(DATA_FILE);
            file.write(mainObj.toString(4)); // Pretty print with 4 spaces indentation
            file.close();
            
            System.out.println("Data saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }
    
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try {
                StringBuilder content = new StringBuilder();
                BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE));
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                }
                reader.close();
                
                JSONObject mainObj = new JSONObject(content.toString());
                JSONArray accountsArray = mainObj.getJSONArray("accounts");
                
                accounts = new HashMap<>();
                for (int i = 0; i < accountsArray.length(); i++) {
                    JSONObject accountJson = accountsArray.getJSONObject(i);
                    Account account = new Account(accountJson);
                    accounts.put(account.getUserId(), account);
                }
                
                System.out.println("Data loaded successfully! " + accounts.size() + " accounts found.");
            } catch (Exception e) {
                System.err.println("Error loading data: " + e.getMessage());
                accounts = new HashMap<>();
            }
        } else {
            accounts = new HashMap<>();
            System.out.println("No existing data found. Starting fresh.");
        }
    }
}

// JSON library - minimal implementation
class JSONObject {
    private HashMap<String, Object> map;
    
    public JSONObject() {
        map = new HashMap<>();
    }
    
    public JSONObject(String jsonString) {
        map = new HashMap<>();
        parseJSON(jsonString);
    }
    
    private void parseJSON(String json) {
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
            String[] pairs = splitJSON(json);
            for (String pair : pairs) {
                int colonIndex = pair.indexOf(":");
                if (colonIndex > 0) {
                    String key = pair.substring(0, colonIndex).trim().replace("\"", "");
                    String value = pair.substring(colonIndex + 1).trim();
                    
                    if (value.startsWith("[")) {
                        map.put(key, new JSONArray(value));
                    } else if (value.startsWith("{")) {
                        map.put(key, new JSONObject(value));
                    } else if (value.startsWith("\"")) {
                        map.put(key, value.substring(1, value.length() - 1));
                    } else if (value.contains(".")) {
                        map.put(key, Double.parseDouble(value));
                    } else {
                        try {
                            map.put(key, Integer.parseInt(value));
                        } catch (NumberFormatException e) {
                            map.put(key, value);
                        }
                    }
                }
            }
        }
    }
    
    private String[] splitJSON(String json) {
        ArrayList<String> parts = new ArrayList<>();
        int braceCount = 0;
        int bracketCount = 0;
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            
            if (!inString) {
                if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                else if (c == '[') bracketCount++;
                else if (c == ']') bracketCount--;
                
                if (c == ',' && braceCount == 0 && bracketCount == 0) {
                    parts.add(current.toString().trim());
                    current = new StringBuilder();
                    continue;
                }
            }
            
            current.append(c);
        }
        
        if (current.length() > 0) {
            parts.add(current.toString().trim());
        }
        
        return parts.toArray(new String[0]);
    }
    
    public void put(String key, Object value) {
        map.put(key, value);
    }
    
    public String getString(String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }
    
    public double getDouble(String key) {
        Object value = map.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return Double.parseDouble(value.toString());
    }
    
    public JSONArray getJSONArray(String key) {
        return (JSONArray) map.get(key);
    }
    
    public String toString(int indent) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        String indentStr = " ".repeat(indent);
        
        int count = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(indentStr).append("\"").append(entry.getKey()).append("\": ");
            Object value = entry.getValue();
            
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof JSONArray) {
                sb.append(((JSONArray) value).toString(indent));
            } else if (value instanceof JSONObject) {
                sb.append(((JSONObject) value).toString(indent));
            } else {
                sb.append(value);
            }
            
            if (count < map.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
            count++;
        }
        
        sb.append("}");
        return sb.toString();
    }
}

class JSONArray {
    private ArrayList<Object> list;
    
    public JSONArray() {
        list = new ArrayList<>();
    }
    
    public JSONArray(ArrayList<?> arrayList) {
        list = new ArrayList<>(arrayList);
    }
    
    public JSONArray(String jsonString) {
        list = new ArrayList<>();
        parseArray(jsonString);
    }
    
    private void parseArray(String json) {
        json = json.trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
            if (json.trim().isEmpty()) return;
            
            String[] elements = splitArrayJSON(json);
            for (String element : elements) {
                element = element.trim();
                if (element.startsWith("\"")) {
                    list.add(element.substring(1, element.length() - 1));
                } else if (element.startsWith("{")) {
                    list.add(new JSONObject(element));
                } else if (element.contains(".")) {
                    list.add(Double.parseDouble(element));
                } else {
                    try {
                        list.add(Integer.parseInt(element));
                    } catch (NumberFormatException e) {
                        list.add(element);
                    }
                }
            }
        }
    }
    
    private String[] splitArrayJSON(String json) {
        ArrayList<String> parts = new ArrayList<>();
        int braceCount = 0;
        int bracketCount = 0;
        StringBuilder current = new StringBuilder();
        boolean inString = false;
        
        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);
            
            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                inString = !inString;
            }
            
            if (!inString) {
                if (c == '{') braceCount++;
                else if (c == '}') braceCount--;
                else if (c == '[') bracketCount++;
                else if (c == ']') bracketCount--;
                
                if (c == ',' && braceCount == 0 && bracketCount == 0) {
                    parts.add(current.toString().trim());
                    current = new StringBuilder();
                    continue;
                }
            }
            
            current.append(c);
        }
        
        if (current.length() > 0) {
            parts.add(current.toString().trim());
        }
        
        return parts.toArray(new String[0]);
    }
    
    public void put(Object value) {
        list.add(value);
    }
    
    public String getString(int index) {
        return list.get(index).toString();
    }
    
    public JSONObject getJSONObject(int index) {
        return (JSONObject) list.get(index);
    }
    
    public int length() {
        return list.size();
    }
    
    public String toString(int indent) {
        if (list.isEmpty()) return "[]";
        
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");
        String indentStr = " ".repeat(indent);
        
        for (int i = 0; i < list.size(); i++) {
            Object value = list.get(i);
            sb.append(indentStr);
            
            if (value instanceof String) {
                sb.append("\"").append(value).append("\"");
            } else if (value instanceof JSONObject) {
                sb.append(((JSONObject) value).toString(indent));
            } else {
                sb.append(value);
            }
            
            if (i < list.size() - 1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        
        sb.append(" ".repeat(indent - 4)).append("]");
        return sb.toString();
    }
}

// Main ATM Interface GUI
public class ATMInterface extends JFrame {
    private ATMManager atmManager;
    private Account currentAccount;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    
    // Panels
    private JPanel welcomePanel;
    private JPanel loginPanel;
    private JPanel menuPanel;
    private JPanel createAccountPanel;
    
    public ATMInterface() {
        atmManager = new ATMManager();
        
        setTitle("ATM System");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Add window listener to save data on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                atmManager.saveData();
            }
        });
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        createWelcomePanel();
        createLoginPanel();
        createAccountCreationPanel();
        createMenuPanel();
        
        add(mainPanel);
        cardLayout.show(mainPanel, "welcome");
        
        setVisible(true);
    }
    
    private void createWelcomePanel() {
        welcomePanel = new JPanel(new GridBagLayout());
        welcomePanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titleLabel = new JLabel("Welcome to ATM System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        welcomePanel.add(titleLabel, gbc);
        
        JButton loginBtn = new JButton("Login to Existing Account");
        loginBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        loginBtn.setPreferredSize(new Dimension(250, 50));
        loginBtn.setBackground(new Color(0, 153, 76));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(e -> cardLayout.show(mainPanel, "login"));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        welcomePanel.add(loginBtn, gbc);
        
        JButton createBtn = new JButton("Create New Account");
        createBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        createBtn.setPreferredSize(new Dimension(250, 50));
        createBtn.setBackground(new Color(0, 102, 204));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        createBtn.addActionListener(e -> cardLayout.show(mainPanel, "createAccount"));
        gbc.gridy = 2;
        welcomePanel.add(createBtn, gbc);
        
        JButton adminBtn = new JButton("Admin Panel");
        adminBtn.setFont(new Font("Arial", Font.PLAIN, 14));
        adminBtn.setPreferredSize(new Dimension(250, 40));
        adminBtn.setBackground(new Color(153, 0, 153));
        adminBtn.setForeground(Color.WHITE);
        adminBtn.setFocusPainted(false);
        adminBtn.addActionListener(e -> showAdminLogin());
        gbc.gridy = 3;
        welcomePanel.add(adminBtn, gbc);
        
        mainPanel.add(welcomePanel, "welcome");
    }
    
    private void showAdminLogin() {
        JPasswordField passwordField = new JPasswordField();
        Object[] message = {
            "Enter Admin Password:", passwordField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String password = new String(passwordField.getPassword());
            if (atmManager.authenticateAdmin(password)) {
                showAdminPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid admin password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showAdminPanel() {
        JDialog adminDialog = new JDialog(this, "Admin Panel - All Accounts", true);
        adminDialog.setSize(800, 600);
        adminDialog.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        StringBuilder sb = new StringBuilder();
        sb.append("-----------------------------------------------------------------------------\n");
        sb.append("                          ALL ACCOUNTS INFORMATION\n");
        sb.append("---------------------------------------------------------------------------\n\n");
        
        HashMap<String, Account> allAccounts = atmManager.getAllAccounts();
        
        if (allAccounts.isEmpty()) {
            sb.append("No accounts found in the system.\n");
        } else {
            int count = 1;
            for (Account account : allAccounts.values()) {
                sb.append("Account #").append(count++).append("\n");
                sb.append("***********************************************************************\n");
                sb.append("User ID      : ").append(account.getUserId()).append("\n");
                sb.append("PIN          : ").append(account.getPin()).append("\n");
                sb.append("Name         : ").append(account.getName()).append("\n");
                sb.append("Balance      : $").append(String.format("%.2f", account.getBalance())).append("\n");
                sb.append("Transactions : ").append(account.getTransactionHistory().size()).append(" total\n");
                sb.append("\n");
            }
            
            sb.append("***************************************************************************\n");
            sb.append("Total Accounts: ").append(allAccounts.size()).append("\n");
            sb.append("Data File: atm_data.json (You can open this file in any text editor)\n");
            sb.append("***************************************************************************\n");
        }
        
        textArea.setText(sb.toString());
        textArea.setCaretPosition(0);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        adminDialog.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel();
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> {
            adminDialog.dispose();
            showAdminPanel();
        });
        buttonPanel.add(refreshBtn);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> adminDialog.dispose());
        buttonPanel.add(closeBtn);
        
        adminDialog.add(buttonPanel, BorderLayout.SOUTH);
        adminDialog.setVisible(true);
    }
    
    private void createLoginPanel() {
        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Login to Your Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        loginPanel.add(titleLabel, gbc);
        
        JLabel userIdLabel = new JLabel("User ID:");
        userIdLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        loginPanel.add(userIdLabel, gbc);
        
        JTextField userIdField = new JTextField(20);
        userIdField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        loginPanel.add(userIdField, gbc);
        
        JLabel pinLabel = new JLabel("PIN:");
        pinLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        loginPanel.add(pinLabel, gbc);
        
        JPasswordField pinField = new JPasswordField(20);
        pinField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        loginPanel.add(pinField, gbc);
        
        JButton loginBtn = new JButton("Login");
        loginBtn.setFont(new Font("Arial", Font.BOLD, 16));
        loginBtn.setPreferredSize(new Dimension(150, 40));
        loginBtn.setBackground(new Color(0, 153, 76));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);
        loginBtn.addActionListener(e -> {
            String userId = userIdField.getText();
            String pin = new String(pinField.getPassword());
            
            Account account = atmManager.authenticate(userId, pin);
            if (account != null) {
                currentAccount = account;
                updateMenuPanel();
                cardLayout.show(mainPanel, "menu");
                userIdField.setText("");
                pinField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Invalid User ID or PIN!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        loginPanel.add(loginBtn, gbc);
        
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(150, 40));
        backBtn.setBackground(new Color(204, 0, 0));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "welcome");
            userIdField.setText("");
            pinField.setText("");
        });
        gbc.gridx = 1;
        loginPanel.add(backBtn, gbc);
        
        mainPanel.add(loginPanel, "login");
    }
    
    private void createAccountCreationPanel() {
        createAccountPanel = new JPanel(new GridBagLayout());
        createAccountPanel.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Create New Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        createAccountPanel.add(titleLabel, gbc);
        
        JLabel nameLabel = new JLabel("Full Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        createAccountPanel.add(nameLabel, gbc);
        
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        createAccountPanel.add(nameField, gbc);
        
        JLabel balanceLabel = new JLabel("Initial Balance:");
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 2;
        createAccountPanel.add(balanceLabel, gbc);
        
        JTextField balanceField = new JTextField(20);
        balanceField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        createAccountPanel.add(balanceField, gbc);
        
        JButton createBtn = new JButton("Create Account");
        createBtn.setFont(new Font("Arial", Font.BOLD, 16));
        createBtn.setPreferredSize(new Dimension(180, 40));
        createBtn.setBackground(new Color(0, 153, 76));
        createBtn.setForeground(Color.WHITE);
        createBtn.setFocusPainted(false);
        createBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String balanceStr = balanceField.getText().trim();
            
            if (name.isEmpty() || balanceStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                double balance = Double.parseDouble(balanceStr);
                if (balance < 0) {
                    JOptionPane.showMessageDialog(this, "Balance cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                String result = atmManager.createAccount(name, balance);
                JOptionPane.showMessageDialog(this, result, "Success", JOptionPane.INFORMATION_MESSAGE);
                nameField.setText("");
                balanceField.setText("");
                cardLayout.show(mainPanel, "welcome");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid balance amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        createAccountPanel.add(createBtn, gbc);
        
        JButton backBtn = new JButton("Back");
        backBtn.setFont(new Font("Arial", Font.PLAIN, 16));
        backBtn.setPreferredSize(new Dimension(180, 40));
        backBtn.setBackground(new Color(204, 0, 0));
        backBtn.setForeground(Color.WHITE);
        backBtn.setFocusPainted(false);
        backBtn.addActionListener(e -> {
            cardLayout.show(mainPanel, "welcome");
            nameField.setText("");
            balanceField.setText("");
        });
        gbc.gridx = 1;
        createAccountPanel.add(backBtn, gbc);
        
        mainPanel.add(createAccountPanel, "createAccount");
    }
    
    private void createMenuPanel() {
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.setBackground(new Color(240, 240, 240));
        mainPanel.add(menuPanel, "menu");
    }
    
    private void updateMenuPanel() {
        menuPanel.removeAll();
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 102, 204));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + currentAccount.getName() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);
        
        JLabel balanceLabel = new JLabel("Balance: $" + String.format("%.2f", currentAccount.getBalance()));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 22));
        balanceLabel.setForeground(Color.WHITE);
        topPanel.add(balanceLabel, BorderLayout.EAST);
        
        menuPanel.add(topPanel, BorderLayout.NORTH);
        
        JPanel buttonPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        buttonPanel.setBackground(new Color(240, 240, 240));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        
        JButton historyBtn = createMenuButton("Transaction History", new Color(0, 153, 153));
        historyBtn.addActionListener(e -> showTransactionHistory());
        buttonPanel.add(historyBtn);
        
        JButton withdrawBtn = createMenuButton("Withdraw", new Color(204, 102, 0));
        withdrawBtn.addActionListener(e -> showWithdrawDialog());
        buttonPanel.add(withdrawBtn);
        
        JButton depositBtn = createMenuButton("Deposit", new Color(0, 153, 76));
        depositBtn.addActionListener(e -> showDepositDialog());
        buttonPanel.add(depositBtn);
        
        JButton transferBtn = createMenuButton("Transfer", new Color(102, 51, 153));
        transferBtn.addActionListener(e -> showTransferDialog());
        buttonPanel.add(transferBtn);
        
        JButton refreshBtn = createMenuButton("Refresh Balance", new Color(0, 102, 204));
        refreshBtn.addActionListener(e -> updateMenuPanel());
        buttonPanel.add(refreshBtn);
        
        JButton quitBtn = createMenuButton("Logout", new Color(204, 0, 0));
        quitBtn.addActionListener(e -> {
            atmManager.saveData();
            currentAccount = null;
            cardLayout.show(mainPanel, "welcome");
        });
        buttonPanel.add(quitBtn);
        
        menuPanel.add(buttonPanel, BorderLayout.CENTER);
        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private JButton createMenuButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        return button;
    }
    
    private void showTransactionHistory() {
        JDialog dialog = new JDialog(this, "Transaction History", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        
        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        ArrayList<String> history = currentAccount.getTransactionHistory();
        for (String transaction : history) {
            textArea.append(transaction + "\n");
        }
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);
        
        JButton closeBtn = new JButton("Close");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeBtn);
        dialog.add(bottomPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void showWithdrawDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to withdraw:");
        if (input != null && !input.isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (currentAccount.withdraw(amount)) {
                    atmManager.saveData();
                    JOptionPane.showMessageDialog(this, "Withdrawal successful!\nNew Balance: $" + String.format("%.2f", currentAccount.getBalance()), "Success", JOptionPane.INFORMATION_MESSAGE);
                    updateMenuPanel();
                } else {
                    JOptionPane.showMessageDialog(this, "Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showDepositDialog() {
        String input = JOptionPane.showInputDialog(this, "Enter amount to deposit:");
        if (input != null && !input.isEmpty()) {
            try {
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                currentAccount.deposit(amount);
                atmManager.saveData();
                JOptionPane.showMessageDialog(this, "Deposit successful!\nNew Balance: $" + String.format("%.2f", currentAccount.getBalance()), "Success", JOptionPane.INFORMATION_MESSAGE);
                updateMenuPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showTransferDialog() {
        ArrayList<String> userIds = atmManager.getAllUserIds();
        if (userIds.size() <= 1) {
            JOptionPane.showMessageDialog(this, "No other accounts available for transfer!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String recipientId = JOptionPane.showInputDialog(this, "Enter recipient User ID:");
        if (recipientId != null && !recipientId.isEmpty()) {
            if (recipientId.equals(currentAccount.getUserId())) {
                JOptionPane.showMessageDialog(this, "Cannot transfer to your own account!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            Account recipient = atmManager.getAccountByUserId(recipientId);
            if (recipient == null) {
                JOptionPane.showMessageDialog(this, "Recipient account not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String input = JOptionPane.showInputDialog(this, "Enter amount to transfer to " + recipient.getName() + ":");
            if (input != null && !input.isEmpty()) {
                try {
                    double amount = Double.parseDouble(input);
                    if (amount <= 0) {
                        JOptionPane.showMessageDialog(this, "Amount must be positive!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (currentAccount.transfer(recipient, amount)) {
                        atmManager.saveData();
                        JOptionPane.showMessageDialog(this, "Transfer successful!\nNew Balance: $" + String.format("%.2f", currentAccount.getBalance()), "Success", JOptionPane.INFORMATION_MESSAGE);
                        updateMenuPanel();
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ATMInterface());
    }
}
