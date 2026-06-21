package datastructures;

import model.Book;
import java.util.ArrayList;
import java.util.List;

public class BookAVL {
    private AVLNode root;
    private int size;

    public BookAVL() {
        this.root = null;
        this.size = 0;
    }

    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int balanceFactor(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private void updateHeight(AVLNode node) {
        node.height = 1 + Math.max(height(node.left), height(node.right));
    }

    private AVLNode rotateRight(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private AVLNode rotateLeft(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private AVLNode balance(AVLNode node) {
        if (node == null)
            return null;

        updateHeight(node);
        int bf = balanceFactor(node);

        if (bf > 1 && balanceFactor(node.left) >= 0)
            return rotateRight(node);

        if (bf > 1 && balanceFactor(node.left) < 0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }

        if (bf < -1 && balanceFactor(node.right) <= 0)
            return rotateLeft(node);

        if (bf < -1 && balanceFactor(node.right) > 0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;
    }

    public void insert(Book book) {
        if (book == null)
            throw new IllegalArgumentException("Book cannot be null");
        root = insertRec(root, book);
    }

    private AVLNode insertRec(AVLNode node, Book book) {
        if (node == null) {
            size++;
            return new AVLNode(book);
        }

        int cmp = book.getIsbn().compareTo(node.book.getIsbn());
        if (cmp < 0)
            node.left = insertRec(node.left, book);
        else if (cmp > 0)
            node.right = insertRec(node.right, book);
        else
            throw new IllegalArgumentException("Book with ISBN [" + book.getIsbn() + "] already exists.");

        return balance(node);
    }

    public Book search(String isbn) {
        if (isbn == null || isbn.trim().isEmpty())
            return null;
        AVLNode result = searchRec(root, isbn.trim());
        return result != null ? result.book : null;
    }

    private AVLNode searchRec(AVLNode node, String isbn) {
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

    private AVLNode deleteRec(AVLNode node, String isbn) {
        if (node == null)
            return null;

        int cmp = isbn.compareTo(node.book.getIsbn());
        if (cmp < 0)
            node.left = deleteRec(node.left, isbn);
        else if (cmp > 0)
            node.right = deleteRec(node.right, isbn);
        else {
            if (node.left == null) {
                size--;
                return node.right;
            }
            if (node.right == null) {
                size--;
                return node.left;
            }
            AVLNode minNode = findMin(node.right);
            node.book = minNode.book;
            node.right = deleteRec(node.right, minNode.book.getIsbn());
        }

        return balance(node);
    }

    private AVLNode findMin(AVLNode node) {
        while (node.left != null)
            node = node.left;
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

    private void inOrderRec(AVLNode node, List<Book> result) {
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

    private void searchByTitleRec(AVLNode node, String keyword, List<Book> result) {
        if (node == null)
            return;
        if (node.book.getTitle().toLowerCase().contains(keyword))
            result.add(node.book);
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

    private void searchByAuthorRec(AVLNode node, String authorName, List<Book> result) {
        if (node == null)
            return;
        if (node.book.getAuthorName().toLowerCase().contains(authorName))
            result.add(node.book);
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
        return height(root);
    }

    public boolean isBalanced() {
        return isBalancedRec(root);
    }

    private boolean isBalancedRec(AVLNode node) {
        if (node == null)
            return true;
        int bf = balanceFactor(node);
        return Math.abs(bf) <= 1 && isBalancedRec(node.left) && isBalancedRec(node.right);
    }
}