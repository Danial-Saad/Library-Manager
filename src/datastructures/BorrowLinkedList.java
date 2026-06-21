package datastructures;

import model.BorrowRecord;
import java.util.ArrayList;
import java.util.List;

public class BorrowLinkedList {
    private BorrowNode head;
    private BorrowNode tail;
    private int size;

    public BorrowLinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public void addRecord(BorrowRecord record) {
        if (record == null)
            return;

        BorrowNode newNode = new BorrowNode(record);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public BorrowRecord findActiveRecord(String studentId, String isbn) {
        if (studentId == null || studentId.trim().isEmpty() || isbn == null || isbn.trim().isEmpty()) {
            return null;
        }

        studentId = studentId.trim();
        isbn = isbn.trim();

        BorrowNode current = head;
        while (current != null) {
            BorrowRecord r = current.record;
            if (r.getStudentId().equals(studentId)
                    && r.getIsbn().equals(isbn)
                    && !r.isReturned()) {
                return r;
            }
            current = current.next;
        }
        return null;
    }

    public boolean markReturned(String studentId, String isbn) {
        BorrowRecord record = findActiveRecord(studentId, isbn);
        if (record == null)
            return false;
        record.markReturned();
        return true;
    }

    public List<BorrowRecord> getRecordsByStudent(String studentId) {
        List<BorrowRecord> result = new ArrayList<>();
        if (studentId == null || studentId.trim().isEmpty())
            return result;

        studentId = studentId.trim();
        BorrowNode current = head;
        while (current != null) {
            if (current.record.getStudentId().equals(studentId)) {
                result.add(current.record);
            }
            current = current.next;
        }
        return result;
    }

    public List<BorrowRecord> getRecordsByIsbn(String isbn) {
        List<BorrowRecord> result = new ArrayList<>();
        if (isbn == null || isbn.trim().isEmpty())
            return result;

        isbn = isbn.trim();
        BorrowNode current = head;
        while (current != null) {
            if (current.record.getIsbn().equals(isbn)) {
                result.add(current.record);
            }
            current = current.next;
        }
        return result;
    }

    public List<BorrowRecord> getActiveRecords() {
        List<BorrowRecord> result = new ArrayList<>();
        BorrowNode current = head;
        while (current != null) {
            if (!current.record.isReturned()) {
                result.add(current.record);
            }
            current = current.next;
        }
        return result;
    }

    public List<BorrowRecord> getOverdueRecords() {
        List<BorrowRecord> result = new ArrayList<>();
        BorrowNode current = head;
        while (current != null) {
            if (current.record.isOverdue()) {
                result.add(current.record);
            }
            current = current.next;
        }
        return result;
    }

    public List<BorrowRecord> getAllRecords() {
        List<BorrowRecord> result = new ArrayList<>();
        BorrowNode current = head;
        while (current != null) {
            result.add(current.record);
            current = current.next;
        }
        return result;
    }

    public int countBorrowsByIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty())
            return 0;

        isbn = isbn.trim();
        int count = 0;
        BorrowNode current = head;
        while (current != null) {
            if (current.record.getIsbn().equals(isbn)) {
                count++;
            }
            current = current.next;
        }
        return count;
    }

    public int countBorrowsByStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty())
            return 0;

        studentId = studentId.trim();
        int count = 0;
        BorrowNode current = head;
        while (current != null) {
            if (current.record.getStudentId().equals(studentId)
                    && !current.record.isReturned()) {
                count++;
            }
            current = current.next;
        }
        return count;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return head == null;
    }
}