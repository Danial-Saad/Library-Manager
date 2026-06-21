package datastructures;

import model.BorrowRecord;

public class BorrowNode {
    BorrowRecord record;
    BorrowNode next;

    public BorrowNode(BorrowRecord record) {
        this.record = record;
        this.next = null;
    }
}
