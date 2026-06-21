package datastructures;

import model.Book;

public class AVLNode {
    Book book;
    AVLNode left;
    AVLNode right;
    int height;

    public AVLNode(Book book) {
        this.book = book;
        this.left = null;
        this.right = null;
        this.height = 1;
    }
}
