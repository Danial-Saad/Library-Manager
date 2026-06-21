package gui;

import manager.LibraryManager;
import model.Book;
import model.Student;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class ReportsPanel extends JPanel {
    private LibraryManager manager;
    private JTextArea displayArea;

    private Color bgWhite = Color.WHITE;
    private Color accentBeige = new Color(205, 170, 125);
    private Color textBlack = new Color(30, 30, 30);

    public ReportsPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(20, 20));
        setBackground(bgWhite);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.setBackground(bgWhite);

        JLabel header = new JLabel("System Analytics & Reports");
        header.setFont(new Font("Century Gothic", Font.BOLD, 28));
        header.setForeground(textBlack);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBeige));
        topPanel.add(header, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setBackground(bgWhite);

        JButton btnGenerate = new JButton("Generate Full Report");
        btnGenerate.setFont(new Font("Century Gothic", Font.BOLD, 16));
        btnGenerate.setBackground(textBlack);
        btnGenerate.setForeground(bgWhite);
        btnGenerate.setFocusPainted(false);
        btnGenerate.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnGenerate.setPreferredSize(new Dimension(220, 45));

        btnGenerate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnGenerate.setBackground(accentBeige);
                btnGenerate.setForeground(textBlack);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnGenerate.setBackground(textBlack);
                btnGenerate.setForeground(bgWhite);
            }
        });

        buttonPanel.add(btnGenerate);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 15));
        displayArea.setForeground(textBlack);
        displayArea.setBackground(new Color(252, 250, 248));

        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(accentBeige, 1));
        add(scrollPane, BorderLayout.CENTER);

        btnGenerate.addActionListener(e -> generateReportAction());
    }

    private void generateReportAction() {
        try {
            List<Book> allBooks = manager.getBooks().inOrder();
            Map<String, Object> stats = manager.getLibraryStats(allBooks);

            StringBuilder sb = new StringBuilder();
            sb.append("=========================================================\n");
            sb.append("                LIBRARY SYSTEM - ANALYTICS               \n");
            sb.append("=========================================================\n\n");

            sb.append("[ OVERVIEW ]\n");
            sb.append("Total Registered Students : ").append(stats.get("totalStudents")).append("\n");
            sb.append("Total Borrow Records      : ").append(stats.get("totalBorrowRecords")).append("\n");
            sb.append("Currently Active Borrows  : ").append(stats.get("activeBorrows")).append("\n");
            sb.append("Overdue Books             : ").append(stats.get("overdueCount")).append("\n");
            sb.append("Total Waitlist Entries    : ").append(stats.get("waitlistSize")).append("\n\n");

            Book mostBorrowed = (Book) stats.get("mostBorrowedBook");
            sb.append("[ POPULARITY ]\n");
            if (mostBorrowed != null) {
                sb.append("Most Borrowed Book        : ").append(mostBorrowed.getTitle()).append("\n");
                sb.append("Author                    : ").append(mostBorrowed.getAuthorName()).append("\n");
                sb.append("Total Borrows             : ").append(manager.getBorrowCountForBook(mostBorrowed.getIsbn()))
                        .append("\n\n");
            } else {
                sb.append("Most Borrowed Book        : No data available yet.\n\n");
            }

            List<Student> overdueStudents = manager.getStudentsWithOverdueBooks();
            sb.append("[ ALERTS: OVERDUE STUDENTS ]\n");
            if (overdueStudents.isEmpty()) {
                sb.append("Great! No students have overdue books.\n");
            } else {
                for (Student s : overdueStudents) {
                    sb.append(" - ").append(s.getName()).append(" (ID: ").append(s.getId()).append(")\n");
                }
            }
            sb.append("\n=========================================================");

            displayArea.setText(sb.toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating report.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}