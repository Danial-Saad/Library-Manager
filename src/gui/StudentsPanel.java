package gui;

import manager.LibraryManager;
import model.Student;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class StudentsPanel extends JPanel {
    private LibraryManager manager;
    private JTextField txtId, txtName;
    private JCheckBox chkGraduated;
    private JTextArea displayArea;

    private Color bgWhite = Color.WHITE;
    private Color accentBeige = new Color(205, 170, 125);
    private Color textBlack = new Color(30, 30, 30);

    public StudentsPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(20, 20));
        setBackground(bgWhite);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.setBackground(bgWhite);

        JLabel header = new JLabel("Students Registry");
        header.setFont(new Font("Century Gothic", Font.BOLD, 28));
        header.setForeground(textBlack);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBeige));
        topPanel.add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        inputPanel.setBackground(bgWhite);

        inputPanel.add(createLabel("Student ID:"));
        txtId = createTextField();
        inputPanel.add(txtId);

        inputPanel.add(createLabel("Full Name:"));
        txtName = createTextField();
        inputPanel.add(txtName);

        inputPanel.add(createLabel("Status:"));
        chkGraduated = new JCheckBox("Is Graduated?");
        chkGraduated.setFont(new Font("Century Gothic", Font.PLAIN, 14));
        chkGraduated.setBackground(bgWhite);
        chkGraduated.setForeground(textBlack);
        chkGraduated.setFocusPainted(false);
        inputPanel.add(chkGraduated);

        inputPanel.add(new JLabel(""));
        inputPanel.add(new JLabel(""));

        topPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(bgWhite);

        JButton btnViewAll = createActionButton("View All Students");
        JButton btnRegister = createActionButton("Register Student");

        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnRegister);

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

        btnRegister.addActionListener(e -> registerAction());
        btnViewAll.addActionListener(e -> viewAllAction());
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
        button.setPreferredSize(new Dimension(170, 40));

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

    private void registerAction() {
        try {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            boolean isGraduated = chkGraduated.isSelected();

            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter both ID and Name.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (manager.isStudentRegistered(id)) {
                JOptionPane.showMessageDialog(this, "Student with this ID is already registered.", "Warning",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            manager.registerStudent(new Student(id, name, isGraduated));
            displayArea.setText("Successfully registered student: " + name);
            txtId.setText("");
            txtName.setText("");
            chkGraduated.setSelected(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAllAction() {
        List<Student> students = manager.getAllStudents();
        if (students.isEmpty()) {
            displayArea.setText("No students registered in the system.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Registered Students ===\n\n");
        for (Student s : students) {
            String status = s.isGraduated() ? "Graduated" : "Active";
            sb.append(String.format("ID: %-10s | Name: %-20s | Status: %s\n", s.getId(), s.getName(), status));
        }
        displayArea.setText(sb.toString());
    }
}