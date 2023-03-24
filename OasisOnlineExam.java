import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.Exception;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OasisOnlineExam {
    public static void main(String args[]) {
        try {
            login form = new login();
            form.setSize(435, 150);
            form.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    static class login extends JFrame implements ActionListener {
        JButton signInButton, updateButton;
        JPanel loginPanel;
        JLabel studentNumberLabel, passwordLabel;
        final JTextField studentNumberField, passwordField;

        login() {
            // To create field for Student number input 
            studentNumberLabel = new JLabel();
            studentNumberLabel.setText("Student Number");
            studentNumberField = new JTextField(20);

            // To create field for password input
            passwordLabel = new JLabel();
            passwordLabel.setText("Password");
            passwordField = new JPasswordField(20);

            signInButton = new JButton("Sign in");
            loginPanel = new JPanel(new GridLayout(3, 4));

            updateButton = new JButton("Update Profile");
            loginPanel = new JPanel(new GridLayout(3, 4));

            // Add all labels and text fields to the panel
            loginPanel.add(studentNumberLabel);
            loginPanel.add(studentNumberField);
            loginPanel.add(passwordLabel);
            loginPanel.add(passwordField);
            loginPanel.add(signInButton);
            loginPanel.add(updateButton);
            add(loginPanel, BorderLayout.CENTER);

            // When Button is pressed, next page is shown
            signInButton.addActionListener(this);
            updateButton.addActionListener(this);
            setTitle("Oasis Online Exam Login");
        }

        public void actionPerformed(ActionEvent ae) {

            if (ae.getSource() == updateButton) {
                // if the answer is correct
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                JOptionPane.showInputDialog(panel, "Update Name");
                JOptionPane.showInputDialog(panel, "Update Password");
                JOptionPane.showInputDialog(panel, "Update Address");
                JOptionPane.showInputDialog(panel, "Update College");
                add(panel);
            }

            else {
            String studentNumberValue = studentNumberField.getText();
            String passwordValue = passwordField.getText();

            String studentNumberRegex = "^\\d{6}$";
            Pattern pattern = Pattern.compile(studentNumberRegex);
            Matcher studentNumberMatcher = pattern.matcher(studentNumberValue);

            if (passwordValue.equals("")) {
                JOptionPane.showMessageDialog(this, "Enter Password");
            }
            if (!studentNumberMatcher.matches()) {
                JOptionPane.showMessageDialog(this, "Student Number should be 6 digits");
            } else {
                new OnlineTestBegin(studentNumberValue);
            }
        }}
    }

    static class OnlineTestBegin extends JFrame implements ActionListener {
        JLabel questionLabel;
        JLabel timeLeftLabel;
        JRadioButton radioButton[] = new JRadioButton[6];
        JButton saveAndNextButton, skipButton;
        ButtonGroup buttonGroup;
        int countScore = 0, currentQuestion = 0, x = 1, now = 0;
        int skippedQuestion[] = new int[10];
        Timer timer = new Timer();

        OnlineTestBegin(String s) {
            super(s);
            questionLabel = new JLabel();
            timeLeftLabel = new JLabel();
            add(questionLabel);
            add(timeLeftLabel);
            buttonGroup = new ButtonGroup();
            for (int i = 0; i < radioButton.length; i++) {
                radioButton[i] = new JRadioButton();
                add(radioButton[i]);
                buttonGroup.add(radioButton[i]);
            }
            saveAndNextButton = new JButton("Save and Next");
            skipButton = new JButton("Skip");
            saveAndNextButton.addActionListener(this);
            skipButton.addActionListener(this);
            add(saveAndNextButton);
            add(skipButton);

            set();
            questionLabel.setBounds(30, 40, 435, 20);
            timeLeftLabel.setBounds(20, 20, 435, 20);
            radioButton[0].setBounds(40, 70, 100, 20);
            radioButton[1].setBounds(40, 105, 100, 20);
            radioButton[2].setBounds(40, 140, 100, 20);
            radioButton[3].setBounds(40, 175, 100, 20);

            saveAndNextButton.setBounds(95, 240, 140, 30);
            skipButton.setBounds(270, 240, 150, 30);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLayout(null);
            setLocation(250, 100);
            setVisible(true);
            setSize(600, 350);
            timer.scheduleAtFixedRate(new TimerTask() {
                int i = 800;

                public void run() {
                    timeLeftLabel.setText("Time left (Seconds): " + i);
                    i--;
                    if (i < 0) {
                        timer.cancel();
                        timeLeftLabel.setText("Time Out");
                    }
                }
            }, 0, 1000);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == saveAndNextButton) {
                // if the answer is correct
                if (check()) countScore = countScore + 1;
                currentQuestion++;
                set();
                if (currentQuestion >= 6) {
                    saveAndNextButton.setEnabled(false);
                    skipButton.setText("Result");
                }
            }
            if (e.getActionCommand().equals("Skip")) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
                add(panel);
                JButton backButton = new JButton("Unsolved " + x);
                backButton.addActionListener(this);
                backButton.setBounds(435, 30 * x, 100, 30);
                add(backButton);

                skippedQuestion[x] = currentQuestion;
                x++;
                currentQuestion++;
                set();
                if (currentQuestion >= 6) skipButton.setText("Result");
                setVisible(false);
            }

            for (int i = 0, y = 1; i < x; i++, y++) {
                if (e.getActionCommand().equals("Unsolved " + y)) {
                    if (check()) countScore = countScore + 1;
                    now = currentQuestion;
                    currentQuestion = skippedQuestion[y];
                    set();
                    ((JButton) e.getSource()).setEnabled(false);
                    currentQuestion = now;
                }
            }
            if (e.getActionCommand().equals("Result")) {
                if (check()) countScore = countScore + 1;
                currentQuestion++;
                JOptionPane.showMessageDialog(this, "Score =" + countScore);
                JOptionPane.showMessageDialog(this, "Logout?");

                System.exit(0);
            }
        }

        void set() {
            setTitle("Question");
            radioButton[4].setSelected(true);
            if (currentQuestion == 0) {
                questionLabel.setText("Q1: Which of the following is NOT a programming language?");
                radioButton[0].setText("Java");
                radioButton[1].setText("HTML");
                radioButton[2].setText("Python");
                radioButton[3].setText("C++");

            }
            if (currentQuestion == 1) {
                questionLabel.setText("Q2: Which type of memory is the fastest?");
                radioButton[0].setText("Cache memory");
                radioButton[1].setText("RAM");
                radioButton[2].setText("ROM");
                radioButton[3].setText("Virtual memory");
            }
            if (currentQuestion == 2) {
                questionLabel.setText("Q3: Which of the following is NOT an operating system?");
                radioButton[0].setText("Windows");
                radioButton[1].setText("Linux");
                radioButton[2].setText("MacOS");
                radioButton[3].setText("Java");
            }

            if (currentQuestion == 3) {
                questionLabel.setText("Q4: What does CPU stand for?");
                radioButton[0].setText("Central Processing Unit");
                radioButton[1].setText("Central Performance Unit");
                radioButton[2].setText("Central Programming Unit");
                radioButton[3].setText("Central Processing Area");
            }
            if (currentQuestion == 4) {
                questionLabel.setText("Q5: Which type of network covers the smallest area?");
                radioButton[0].setText("LAN");
                radioButton[1].setText("WAN");
                radioButton[2].setText("MAN");
                radioButton[3].setText("PAN");
            }

            if (currentQuestion == 5) {
                questionLabel.setText("Q6: Which of the following is NOT a programming paradigm?");
                radioButton[0].setText("Object-oriented");
                radioButton[1].setText("Procedural");
                radioButton[2].setText("Functional");
                radioButton[3].setText("Imperative");
            }
            if (currentQuestion == 6) {
                questionLabel.setText("Q7: Which of the following is NOT a cloud computing service?");
                radioButton[0].setText("Amazon Web Services");
                radioButton[1].setText("Microsoft Azure");
                radioButton[2].setText("Dropbox");
                radioButton[3].setText("Oracle Cloud");
            }
            questionLabel.setBounds(30, 40, 435, 20);
            for (int i = 0, j = 0; i <= 90; i += 30, j++)
                radioButton[j].setBounds(50, 80 + i, 200, 20);
        }

        // To check the answer
        boolean check() {
            if (currentQuestion == 0) return (radioButton[1].isSelected());
            if (currentQuestion == 1) return (radioButton[0].isSelected());
            if (currentQuestion == 2) return (radioButton[3].isSelected());
            if (currentQuestion == 3) return (radioButton[0].isSelected());
            if (currentQuestion == 4) return (radioButton[3].isSelected());
            if (currentQuestion == 5) return (radioButton[3].isSelected());
            if (currentQuestion == 6) return (radioButton[2].isSelected());
            return false;
        }
    }
}
