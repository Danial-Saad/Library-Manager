package gui;

import manager.LibraryManager;
import datastructures.WaitlistEntry;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class WaitlistPanel extends JPanel {
    private LibraryManager manager;
    private JTextField txtIsbn;
    private JTextArea displayArea;

    private Color bgWhite = Color.WHITE;
    private Color accentBeige = new Color(205, 170, 125);
    private Color textBlack = new Color(30, 30, 30);

    public WaitlistPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(20, 20));
        setBackground(bgWhite);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.setBackground(bgWhite);

        JLabel header = new JLabel("Waitlist Queue Management");
        header.setFont(new Font("Century Gothic", Font.BOLD, 28));
        header.setForeground(textBlack);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBeige));
        topPanel.add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        inputPanel.setBackground(bgWhite);

        JLabel lblIsbn = new JLabel("Book ISBN:");
        lblIsbn.setFont(new Font("Century Gothic", Font.BOLD, 16));
        lblIsbn.setForeground(textBlack);
        inputPanel.add(lblIsbn);

        txtIsbn = new JTextField(15);
        txtIsbn.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        txtIsbn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(accentBeige, 1),
                new EmptyBorder(5, 10, 5, 10)));
        inputPanel.add(txtIsbn);

        topPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(bgWhite);

        JButton btnViewQueue = new JButton("View Waitlist");
        btnViewQueue.setFont(new Font("Century Gothic", Font.BOLD, 14));
        btnViewQueue.setBackground(textBlack);
        btnViewQueue.setForeground(bgWhite);
        btnViewQueue.setFocusPainted(false);
        btnViewQueue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnViewQueue.setPreferredSize(new Dimension(160, 40));

        btnViewQueue.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnViewQueue.setBackground(accentBeige);
                btnViewQueue.setForeground(textBlack);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnViewQueue.setBackground(textBlack);
                btnViewQueue.setForeground(bgWhite);
            }
        });

        buttonPanel.add(btnViewQueue);
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

        btnViewQueue.addActionListener(e -> viewWaitlistAction());
    }

    private void viewWaitlistAction() {
        String isbn = txtIsbn.getText().trim();
        if (isbn.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ISBN.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            List<WaitlistEntry> waitingList = manager.getWaitlist().getWaitlistForBook(isbn);

            if (waitingList == null || waitingList.isEmpty()) {
                displayArea.setText("No students are currently waiting for ISBN: " + isbn);
                return;
            }

            StringBuilder sb = new StringBuilder();
            sb.append("=== Waitlist for Book ISBN: ").append(isbn).append(" ===\n\n");

            int position = 1;
            for (WaitlistEntry entry : waitingList) {
                String priority = entry.getStudent().isGraduated() ? "[High Priority - Graduated]"
                        : "[Normal Priority]";
                sb.append(position).append(". ")
                        .append(entry.getStudent().getName())
                        .append(" (ID: ").append(entry.getStudent().getId()).append(") ")
                        .append(priority).append("\n");
                position++;
            }

            displayArea.setText(sb.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error fetching waitlist.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}