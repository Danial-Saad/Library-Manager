package gui;

import manager.LibraryManager;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainDashboard extends JFrame {
    private LibraryManager manager;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private Color bgWhite = Color.WHITE;
    private Color accentBeige = new Color(205, 170, 125);
    private Color textBlack = new Color(30, 30, 30);

    public MainDashboard() {
        manager = new LibraryManager();

        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgWhite);

        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(bgWhite);
        sidebar.setPreferredSize(new Dimension(250, 600));
        sidebar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 2, accentBeige),
                new EmptyBorder(30, 15, 30, 15)));

        JLabel logoLabel = new JLabel("Library System");
        logoLabel.setFont(new Font("Century Gothic", Font.BOLD, 24));
        logoLabel.setForeground(textBlack);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logoLabel);

        sidebar.add(Box.createRigidArea(new Dimension(0, 50)));

        JButton btnBooks = createMenuButton("Books Catalog");
        JButton btnStudents = createMenuButton("Students Registry");
        JButton btnBorrow = createMenuButton("Borrow & Return");
        JButton btnWaitlist = createMenuButton("Waitlist Queue");
        JButton btnReports = createMenuButton("System Analytics");

        sidebar.add(btnBooks);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnStudents);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnBorrow);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnWaitlist);
        sidebar.add(Box.createRigidArea(new Dimension(0, 15)));
        sidebar.add(btnReports);

        add(sidebar, BorderLayout.WEST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(bgWhite);

        cardPanel.add(new BooksPanel(manager), "BooksPanel");
        cardPanel.add(new StudentsPanel(manager), "StudentsPanel");
        cardPanel.add(new BorrowPanel(manager), "BorrowPanel");
        cardPanel.add(new WaitlistPanel(manager), "WaitlistPanel");
        cardPanel.add(new ReportsPanel(manager), "ReportsPanel");

        add(cardPanel, BorderLayout.CENTER);

        btnBooks.addActionListener(e -> cardLayout.show(cardPanel, "BooksPanel"));
        btnStudents.addActionListener(e -> cardLayout.show(cardPanel, "StudentsPanel"));
        btnBorrow.addActionListener(e -> cardLayout.show(cardPanel, "BorrowPanel"));
        btnWaitlist.addActionListener(e -> cardLayout.show(cardPanel, "WaitlistPanel"));
        btnReports.addActionListener(e -> cardLayout.show(cardPanel, "ReportsPanel"));
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Century Gothic", Font.BOLD, 15));
        button.setForeground(textBlack);
        button.setBackground(bgWhite);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(accentBeige, 1));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(220, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(accentBeige);
                button.setForeground(bgWhite);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgWhite);
                button.setForeground(textBlack);
            }
        });
        return button;
    }
}