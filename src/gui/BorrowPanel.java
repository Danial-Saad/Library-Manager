package gui;

import manager.LibraryManager;
import model.Student;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;
import java.util.List;

public class BorrowPanel extends JPanel {
    private LibraryManager manager;
    private JTextField txtStudentId, txtIsbn;
    private JTextArea displayArea;

    private Color bgWhite = Color.WHITE;
    private Color accentBeige = new Color(205, 170, 125);
    private Color textBlack = new Color(30, 30, 30);

    public BorrowPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(20, 20));
        setBackground(bgWhite);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.setBackground(bgWhite);

        JLabel header = new JLabel("Borrow & Return Transactions");
        header.setFont(new Font("Century Gothic", Font.BOLD, 28));
        header.setForeground(textBlack);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBeige));
        topPanel.add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        inputPanel.setBackground(bgWhite);

        inputPanel.add(createLabel("Student ID:"));
        txtStudentId = createTextField();
        inputPanel.add(txtStudentId);

        inputPanel.add(createLabel("Book ISBN:"));
        txtIsbn = createTextField();
        inputPanel.add(txtIsbn);

        topPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(bgWhite);

        JButton btnStatus = createActionButton("Student Status");
        JButton btnReturn = createActionButton("Return Book");
        JButton btnBorrow = createActionButton("Borrow Book");

        buttonPanel.add(btnStatus);
        buttonPanel.add(btnReturn);
        buttonPanel.add(btnBorrow);

        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        displayArea.setForeground(textBlack);
        displayArea.setBackground(new Color(252, 250, 248));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(accentBeige, 1));
        add(scrollPane, BorderLayout.CENTER);

        btnBorrow.addActionListener(e -> borrowAction());
        btnReturn.addActionListener(e -> returnAction());
        btnStatus.addActionListener(e -> statusAction());
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Century Gothic", Font.BOLD, 14));
        label.setForeground(textBlack);
        return label;
    }

    private JTextField createTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentBeige, 1),
                new EmptyBorder(5, 10, 5, 10)));
        return field;
    }

    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Century Gothic", Font.BOLD, 14));
        button.setBackground(textBlack);
        button.setForeground(bgWhite);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(accentBeige);
                button.setForeground(textBlack);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(textBlack);
                button.setForeground(bgWhite);
            }
        });
        return button;
    }

    private void borrowAction() {
        try {
            String studentId = txtStudentId.getText().trim();
            String isbn = txtIsbn.getText().trim();

            if (studentId.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both Student ID and ISBN are required.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student student = manager.findStudent(studentId);
            if (student == null) {
                JOptionPane.showMessageDialog(this, "Student not found. Please register first.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = manager.borrowBook(student, isbn);

            if (success) {
                displayArea.setText("Book successfully borrowed by: " + student.getName());
            } else {
                displayArea.setText("Book is unavailable.\nStudent has been added to the waitlist.");
            }
            txtIsbn.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Operation Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void returnAction() {
        try {
            String studentId = txtStudentId.getText().trim();
            String isbn = txtIsbn.getText().trim();

            if (studentId.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Both Student ID and ISBN are required.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = manager.returnBook(studentId, isbn);
            if (success) {
                displayArea.setText("Book returned successfully!\nWaitlist processed automatically if needed.");
            } else {
                displayArea.setText("Return failed. Record not found.");
            }
            txtIsbn.setText("");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void statusAction() {
        try {
            String studentId = txtStudentId.getText().trim();
            if (studentId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a Student ID.", "Input Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Map<String, Object> summary = manager.getStudentSummary(studentId);
            Student student = (Student) summary.get("student");

            if (student == null) {
                displayArea.setText("Student not found.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("=== Status for: ").append(student.getName()).append(" ===\n\n");
            sb.append("Active Borrows: ").append(summary.get("activeBorrowCount")).append(" / 3\n");
            sb.append("Overdue Books: ").append(summary.get("overdueCount")).append("\n");
            sb.append("Total Fines: $").append(String.format("%.2f", (double) summary.get("totalFine"))).append("\n\n");

            List<?> activeBooks = (List<?>) summary.get("activeBorrows");
            if (!activeBooks.isEmpty()) {
                sb.append("Currently Borrowed:\n");
                for (Object obj : activeBooks) {
                    model.BorrowRecord r = (model.BorrowRecord) obj;
                    sb.append(" - ").append(r.getBookTitle()).append(" (Expected Return: ")
                            .append(r.getExpectedReturnDate()).append(")\n");
                }
            }
            displayArea.setText(sb.toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}