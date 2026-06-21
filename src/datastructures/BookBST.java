package datastructures;

import model.Book;
import java.util.ArrayList;
import java.util.List;

public class BookBST {
    private BSTNode root;
    private int size;

    public BookBST() {
        this.root = null;
        this.size = 0;
    }

    public void insert(Book book) {
        if (book == null)
            throw new IllegalArgumentException("Book cannot be null");
        root = insertRec(root, book);
    }

    private BSTNode insertRec(BSTNode node, Book book) {
        if (node == null) {
            size++;
            return new BSTNode(book);
        }

        int cmp = book.getIsbn().compareTo(node.book.getIsbn());
        if (cmp < 0) {
            node.left = insertRec(node.left, book);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, book);
        } else {
            throw new IllegalArgumentException("Book with ISBN [" + book.getIsbn() + "] already exists.");
        }
        return node;
    }

    public Book search(String isbn) {
        if (isbn == null || isbn.trim().isEmpty())
            return null;
        BSTNode result = searchRec(root, isbn.trim());
        return result != null ? result.book : null;
    }

    private BSTNode searchRec(BSTNode node, String isbn) {
        if (node == null)
            return null;
        int cmp = isbn.compareTo(node.book.getIsbn());
        if (cmp == 0)
            return node;
        if (cmp < 0)
            return searchRec(node.left, isbn);
        return searchRec(node.right, isbn);
    }

    public boolean delete(String isbn) {
        if (isbn == null || isbn.trim().isEmpty())
            return false;
        int oldSize = size;
        root = deleteRec(root, isbn.trim());
        return size < oldSize;
    }

    private BSTNode deleteRec(BSTNode node, String isbn) {
        if (node == null)
            return null;

        int cmp = isbn.compareTo(node.book.getIsbn());
        if (cmp < 0) {
            node.left = deleteRec(node.left, isbn);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, isbn);
        } else {
            if (node.left == null) {
                size--;
                return node.right;
            }
            if (node.right == null) {
                size--;
                return node.left;
            }

            BSTNode minNode = findMin(node.right);
            node.book = minNode.book;
            node.right = deleteRec(node.right, minNode.book.getIsbn());
        }
        return node;
    }

    private BSTNode findMin(BSTNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public boolean updateCopies(String isbn, int newTotal) {
        Book book = search(isbn);
        if (book == null)
            return false;

        int borrowed = book.getBorrowedCopies();
        if (newTotal < borrowed)
            return false;

        book.setTotalCopies(newTotal);
        book.setAvailableCopies(newTotal - borrowed);
        return true;
    }

    public List<Book> inOrder() {
        List<Book> result = new ArrayList<>();
        inOrderRec(root, result);
        return result;
    }

    private void inOrderRec(BSTNode node, List<Book> result) {
        if (node == null)
            return;
        inOrderRec(node.left, result);
        result.add(node.book);
        inOrderRec(node.right, result);
    }

    public List<Book> searchByTitle(String keyword) {
        List<Book> result = new ArrayList<>();
        if (keyword == null || keyword.trim().isEmpty())
            return result;
        searchByTitleRec(root, keyword.toLowerCase().trim(), result);
        return result;
    }

    private void searchByTitleRec(BSTNode node, String keyword, List<Book> result) {
        if (node == null)
            return;
        if (node.book.getTitle().toLowerCase().contains(keyword)) {
            result.add(node.book);
        }
        searchByTitleRec(node.left, keyword, result);
        searchByTitleRec(node.right, keyword, result);
    }

    public List<Book> searchByAuthor(String authorName) {
        List<Book> result = new ArrayList<>();
        if (authorName == null || authorName.trim().isEmpty())
            return result;
        searchByAuthorRec(root, authorName.toLowerCase().trim(), result);
        return result;
    }

    private void searchByAuthorRec(BSTNode node, String authorName, List<Book> result) {
        if (node == null)
            return;
        if (node.book.getAuthorName().toLowerCase().contains(authorName)) {
            result.add(node.book);
        }
        searchByAuthorRec(node.left, authorName, result);
        searchByAuthorRec(node.right, authorName, result);
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int height() {
        return heightRec(root);
    }

    private int heightRec(BSTNode node) {
        if (node == null)
            return 0;
        return 1 + Math.max(heightRec(node.left), heightRec(node.right));
    }
}