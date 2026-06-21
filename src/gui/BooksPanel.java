package gui;

import manager.LibraryManager;
import model.Book;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class BooksPanel extends JPanel {
    private LibraryManager manager;
    private JTextField txtIsbn, txtTitle, txtAuthor, txtCopies;
    private JTextArea displayArea;

    private Color bgWhite = Color.WHITE;
    private Color accentBeige = new Color(205, 170, 125);
    private Color textBlack = new Color(30, 30, 30);

    public BooksPanel(LibraryManager manager) {
        this.manager = manager;
        setLayout(new BorderLayout(20, 20));
        setBackground(bgWhite);
        setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel topPanel = new JPanel(new BorderLayout(0, 20));
        topPanel.setBackground(bgWhite);

        JLabel header = new JLabel("Books Catalog");
        header.setFont(new Font("Century Gothic", Font.BOLD, 28));
        header.setForeground(textBlack);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, accentBeige));
        topPanel.add(header, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 15, 15));
        inputPanel.setBackground(bgWhite);

        inputPanel.add(createLabel("ISBN:"));
        txtIsbn = createTextField();
        inputPanel.add(txtIsbn);

        inputPanel.add(createLabel("Title:"));
        txtTitle = createTextField();
        inputPanel.add(txtTitle);

        inputPanel.add(createLabel("Author:"));
        txtAuthor = createTextField();
        inputPanel.add(txtAuthor);

        inputPanel.add(createLabel("Total Copies:"));
        txtCopies = createTextField();
        inputPanel.add(txtCopies);

        topPanel.add(inputPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        buttonPanel.setBackground(bgWhite);

        JButton btnViewAll = createActionButton("View All Books");
        JButton btnSearch = createActionButton("Search");
        JButton btnAdd = createActionButton("Add Book");

        buttonPanel.add(btnViewAll);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnAdd);

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

        btnAdd.addActionListener(e -> addBookAction());
        btnSearch.addActionListener(e -> searchBookAction());
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

    private void addBookAction() {
        try {
            String isbn = txtIsbn.getText().trim();
            String title = txtTitle.getText().trim();
            String author = txtAuthor.getText().trim();
            int copies = Integer.parseInt(txtCopies.getText().trim());

            if (isbn.isEmpty() || title.isEmpty() || author.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            manager.addBook(new Book(isbn, title, author, copies));
            displayArea.setText("Successfully added: " + title);
            txtIsbn.setText("");
            txtTitle.setText("");
            txtAuthor.setText("");
            txtCopies.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Copies must be a valid number.", "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void searchBookAction() {
        try {
            String isbn = txtIsbn.getText().trim();
            if (isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an ISBN to search.", "Input Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            Book book = manager.findBook(isbn);
            if (book != null) {
                displayArea.setText(String.format("Book Found:\nTitle: %s\nAuthor: %s\nAvailable Copies: %d / %d",
                        book.getTitle(), book.getAuthorName(), book.getAvailableCopies(), book.getTotalCopies()));
            } else {
                displayArea.setText("No book found with ISBN: " + isbn);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void viewAllAction() {
        List<Book> allBooks = manager.getBooks().inOrder();
        if (allBooks.isEmpty()) {
            displayArea.setText("The library is currently empty.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== Library Catalog ===\n\n");
        for (Book b : allBooks) {
            sb.append(String.format("ISBN: %-10s | Title: %-25s | Available: %d/%d\n",
                    b.getIsbn(), b.getTitle(), b.getAvailableCopies(), b.getTotalCopies()));
        }
        displayArea.setText(sb.toString());
    }
}