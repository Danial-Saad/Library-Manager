package datastructures;

import model.Book;

public class BSTNode {
    Book book;
    BSTNode left;
    BSTNode right;

    public BSTNode(Book book) {
        this.book = book;
        this.left = null;
        this.right = null;
    }
}
