import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;
import java.util.ArrayList;
import java.util.Scanner;


public class OasisATM extends JFrame implements ActionListener {
    private JTextField CustomerIDField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;


    public static void main(String[] args) {
        new OasisATM();
    }

    public OasisATM() {
        // Set up the frame
        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the components
        CustomerIDField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        errorLabel = new JLabel();

        // Create the panel and add the components
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Customer ID: "));
        panel.add(CustomerIDField);
        panel.add(new JLabel("Password: "));
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);

        // Add the error label to the frame
        add(errorLabel, BorderLayout.SOUTH);

        // Add the panel to the frame
        add(panel);

        // Make the frame visible
        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String username = CustomerIDField.getText();
        String password = new String(passwordField.getPassword());

        // Check if username and password are valid using regex
        Pattern pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]+$");
        Matcher usernameMatcher = pattern.matcher(username);
        Matcher passwordMatcher = pattern.matcher(password);

        if (!usernameMatcher.matches()) {
            errorLabel.setText("Customer must ID includes at least one character and number");
            return;
        }

        if (!passwordMatcher.matches()) {
            errorLabel.setText("Password must includes at least one character and number");
            return;
        }

        // If both username and password are valid, proceed with login
        setVisible(false);
        ATM();
    }

    private static int balance = 1000; // initial balance
    private static DefaultListModel<String> transactionListModel = new DefaultListModel<>();
    private static int transactionOrder = 0;


    public static void ATM() {
        // Create a JFrame window
        JFrame frame = new JFrame("ATM Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // Create a JPanel and add it to the frame
        JPanel panel = new JPanel();
        frame.add(panel);

        // Create a JLabel to display the balance
        JLabel balanceLabel = new JLabel("Balance: " + balance);
        panel.add(balanceLabel);

        // Create a JButton to withdraw money
        JButton withdrawButton = new JButton("Withdraw");
        withdrawButton.addActionListener(e -> {
            String amountString = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
            try {
                int amount = Integer.parseInt(amountString);
                if (amount > balance) {
                    JOptionPane.showMessageDialog(frame, "Insufficient balance!");
                } else {
                    balance -= amount;
                    balanceLabel.setText("Balance: " + balance);
                    JOptionPane.showMessageDialog(frame, "Amount withdrawn: " + amount + "\nRemaining balance: " + balance);
                    transactionListModel.add(transactionOrder, "Withdraw amount: " + amount);
                    transactionOrder++;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount!");
            }
        });
        panel.add(withdrawButton);

        // Create a JButton to deposit money
        JButton depositButton = new JButton("Deposit");
        depositButton.addActionListener(e -> {
            String amountString = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
            try {
                int amount = Integer.parseInt(amountString);
                balance += amount;
                balanceLabel.setText("Balance: " + balance);
                JOptionPane.showMessageDialog(frame, "Amount deposited: " + amount + "\nNew balance: " + balance);
                transactionListModel.add(transactionOrder, "Deposit amount: " + amount);
                transactionOrder++;
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount!");
            }
        });
        panel.add(depositButton);

        // Create a JButton to transfer money
        JButton transferButton = new JButton("Transfer");
        String bankAccountRegex = "^\\d{8}$";

        transferButton.addActionListener(e -> {
            String transferString = JOptionPane.showInputDialog(frame, "Enter amount to transfer:");
            String transferString2 = JOptionPane.showInputDialog(frame, "Enter bank account of receiver:");

            try {
                if (transferString2.matches(bankAccountRegex)) {
                    int amount = Integer.parseInt(transferString);
                    if (amount > balance) {
                        JOptionPane.showMessageDialog(frame, "Insufficient balance!");
                    } else {
                        balance -= amount;
                        balanceLabel.setText("Balance: " + balance);
                        JOptionPane.showMessageDialog(frame, "Amount: " + amount + "\nReceiver Bank Account: " + transferString2 + "\nNew balance: " + balance);
                        transactionListModel.add(transactionOrder, "Transfer amount: " + amount);
                        transactionOrder++;
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Bank Account should be 8 digit numbers:");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount!");
            }
        });
        panel.add(transferButton);

        // Create a JButton to check Transaction History
        JButton viewHistoryButton = new JButton("Transaction History");
        viewHistoryButton.addActionListener(e ->

        {
            if (!transactionListModel.isEmpty()) {
                JOptionPane.showMessageDialog(frame, transactionListModel.toArray());
            } else {
                JOptionPane.showMessageDialog(frame, "No Transaction History!");
            }
        });
        panel.add(viewHistoryButton);

        // Create a JButton to quit the program
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));
        panel.add(quitButton);

        // Show the JFrame window
        frame.setVisible(true);
    }
}
