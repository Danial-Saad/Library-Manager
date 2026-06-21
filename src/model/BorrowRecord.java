package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BorrowRecord {
    private String studentId;
    private String studentName;
    private String isbn;
    private String bookTitle;
    private LocalDate borrowDate;
    private LocalDate expectedReturnDate;
    private boolean returned;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public BorrowRecord(String studentId, String studentName, String isbn, String bookTitle,
                        LocalDate borrowDate, LocalDate expectedReturnDate) {
        if (studentId == null || studentId.trim().isEmpty())
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        if (studentName == null || studentName.trim().isEmpty())
            throw new IllegalArgumentException("Student name cannot be null or empty");
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        if (bookTitle == null || bookTitle.trim().isEmpty())
            throw new IllegalArgumentException("Book title cannot be null or empty");
        if (borrowDate == null)
            throw new IllegalArgumentException("Borrow date cannot be null");
        if (expectedReturnDate == null)
            throw new IllegalArgumentException("Expected return date cannot be null");
        if (expectedReturnDate.isBefore(borrowDate))
            throw new IllegalArgumentException("Return date cannot be before borrow date");

        this.studentId = studentId.trim();
        this.studentName = studentName.trim();
        this.isbn = isbn.trim();
        this.bookTitle = bookTitle.trim();
        this.borrowDate = borrowDate;
        this.expectedReturnDate = expectedReturnDate;
        this.returned = false;
    }

    public boolean isOverdue() {
        return !returned && LocalDate.now().isAfter(expectedReturnDate);
    }

    public void markReturned() {
        if (returned)
            throw new IllegalStateException("Record is already marked as returned");
        this.returned = true;
    }

    public String getStudentId() { return studentId; }
    public String getStudentName() { return studentName; }
    public String getIsbn() { return isbn; }
    public String getBookTitle() { return bookTitle; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public LocalDate getExpectedReturnDate() { return expectedReturnDate; }
    public boolean isReturned() { return returned; }

    public String getBorrowDateStr() { return borrowDate.format(FORMATTER); }
    public String getExpectedReturnDateStr() { return expectedReturnDate.format(FORMATTER); }

    @Override
    public String toString() {
        return studentName + " | " + bookTitle
               + " | Borrowed: " + getBorrowDateStr()
               + " | Due: " + getExpectedReturnDateStr()
               + (returned ? " [RETURNED]" : isOverdue() ? " [OVERDUE]" : " [ACTIVE]");
    }
}
