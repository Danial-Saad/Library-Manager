package manager;

import datastructures.BookAVL;
import datastructures.BookWaitlistQueue;
import datastructures.BorrowLinkedList;
import datastructures.WaitlistEntry;
import model.Book;
import model.BorrowRecord;
import model.Student;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryManager {

    private BookAVL books;
    private BorrowLinkedList borrowRecords;
    private BookWaitlistQueue waitlist;
    private Map<String, Student> students;

    private static final int MAX_BORROW_LIMIT = 3;
    private static final int BORROW_DURATION_DAYS = 14;
    private static final double FINE_PER_DAY = 0.50;

    public LibraryManager() {
        this.books = new BookAVL();
        this.borrowRecords = new BorrowLinkedList();
        this.waitlist = new BookWaitlistQueue();
        this.students = new HashMap<>();
    }

    public BookAVL getBooks() {
        return books;
    }

    public BorrowLinkedList getBorrowRecords() {
        return borrowRecords;
    }

    public BookWaitlistQueue getWaitlist() {
        return waitlist;
    }

    public void addBook(Book book) {
        if (book == null)
            return;
        books.insert(book);
    }

    public Book findBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        return books.search(isbn.trim());
    }

    public void updateBookCopies(String isbn, int newTotal) {
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        if (newTotal <= 0)
            throw new IllegalArgumentException("Total copies must be greater than zero");
        books.updateCopies(isbn.trim(), newTotal);
    }

    public void updateBookTitle(String isbn, String newTitle) {
        if (isbn == null || isbn.trim().isEmpty() || newTitle == null || newTitle.trim().isEmpty())
            throw new IllegalArgumentException("ISBN and title cannot be null or empty");
        Book book = books.search(isbn.trim());
        if (book == null)
            throw new IllegalArgumentException("Book not found: " + isbn);
        book.setTitle(newTitle.trim());
    }

    public void updateBookAuthor(String isbn, String newAuthor) {
        if (isbn == null || isbn.trim().isEmpty() || newAuthor == null || newAuthor.trim().isEmpty())
            throw new IllegalArgumentException("ISBN and author cannot be null or empty");
        Book book = books.search(isbn.trim());
        if (book == null)
            throw new IllegalArgumentException("Book not found: " + isbn);
        book.setAuthorName(newAuthor.trim());
    }

    public void registerStudent(Student student) {
        if (student == null)
            throw new IllegalArgumentException("Student cannot be null");
        students.put(student.getId(), student);
    }

    public Student findStudent(String studentId) {
        if (studentId == null || studentId.trim().isEmpty())
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        return students.get(studentId.trim());
    }

    public boolean isStudentRegistered(String studentId) {
        return students.containsKey(studentId != null ? studentId.trim() : "");
    }

    public void updateStudentGraduationStatus(String studentId, boolean isGraduated) {
        if (studentId == null || studentId.trim().isEmpty())
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        Student student = students.get(studentId.trim());
        if (student == null)
            throw new IllegalArgumentException("Student not found: " + studentId);
        student.setGraduated(isGraduated);
    }

    public boolean borrowBook(Student student, String isbn) {
        if (student == null || isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("Invalid student or ISBN");

        isbn = isbn.trim();

        int currentBorrows = borrowRecords.countBorrowsByStudent(student.getId());
        if (currentBorrows >= MAX_BORROW_LIMIT)
            throw new IllegalStateException(
                    "Student [" + student.getName() + "] has reached the maximum borrow limit of " + MAX_BORROW_LIMIT);

        Book book = books.search(isbn);
        if (book == null)
            throw new IllegalArgumentException("Book not found: " + isbn);

        if (borrowRecords.findActiveRecord(student.getId(), isbn) != null)
            throw new IllegalStateException(
                    "Student [" + student.getName() + "] already has an active borrow for this book");

        if (!book.isAvailable()) {
            waitlist.enqueue(student, isbn);
            return false;
        }

        book.borrowCopy();
        LocalDate borrowDate = LocalDate.now();
        LocalDate expectedReturnDate = borrowDate.plusDays(BORROW_DURATION_DAYS);

        BorrowRecord record = new BorrowRecord(
                student.getId(),
                student.getName(),
                isbn,
                book.getTitle(),
                borrowDate,
                expectedReturnDate);

        borrowRecords.addRecord(record);
        return true;
    }

    public boolean returnBook(String studentId, String isbn) {
        if (studentId == null || studentId.trim().isEmpty() || isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("Invalid studentId or ISBN");

        studentId = studentId.trim();
        isbn = isbn.trim();

        boolean marked = borrowRecords.markReturned(studentId, isbn);
        if (!marked)
            throw new IllegalStateException("No active borrow record found for student: " + studentId);

        Book book = books.search(isbn);
        if (book == null)
            throw new IllegalArgumentException("Book not found: " + isbn);

        book.returnCopy();
        processWaitlistForBook(book);

        return true;
    }

    private void processWaitlistForBook(Book book) {
        if (book == null)
            return;
        String isbn = book.getIsbn();

        while (book.isAvailable()) {
            List<WaitlistEntry> waiting = waitlist.getWaitlistForBook(isbn);
            if (waiting.isEmpty())
                break;

            WaitlistEntry nextEntry = waiting.get(0);
            waitlist.removeStudent(nextEntry.getStudent().getId(), isbn);

            book.borrowCopy();
            LocalDate borrowDate = LocalDate.now();
            LocalDate expectedReturnDate = borrowDate.plusDays(BORROW_DURATION_DAYS);

            BorrowRecord record = new BorrowRecord(
                    nextEntry.getStudent().getId(),
                    nextEntry.getStudent().getName(),
                    isbn,
                    book.getTitle(),
                    borrowDate,
                    expectedReturnDate);
            borrowRecords.addRecord(record);
        }
    }

    public List<BorrowRecord> getActiveBorrows() {
        return borrowRecords.getActiveRecords();
    }

    public List<BorrowRecord> getOverdueBorrows() {
        return borrowRecords.getOverdueRecords();
    }

    public List<BorrowRecord> getStudentHistory(String studentId) {
        if (studentId == null || studentId.trim().isEmpty())
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        return borrowRecords.getRecordsByStudent(studentId.trim());
    }

    public int getBorrowCountForBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        return borrowRecords.countBorrowsByIsbn(isbn.trim());
    }

    public Book getMostBorrowedBook(List<Book> allBooks) {
        if (allBooks == null || allBooks.isEmpty())
            return null;
        Book most = null;
        int max = -1;
        for (Book b : allBooks) {
            int count = borrowRecords.countBorrowsByIsbn(b.getIsbn());
            if (count > max) {
                max = count;
                most = b;
            }
        }
        return most;
    }

    public double calculateFine(String studentId, String isbn) {
        if (studentId == null || isbn == null)
            return 0.0;
        BorrowRecord record = borrowRecords.findActiveRecord(studentId.trim(), isbn.trim());
        if (record == null || !record.isOverdue())
            return 0.0;
        long daysLate = ChronoUnit.DAYS.between(record.getExpectedReturnDate(), LocalDate.now());
        return daysLate * FINE_PER_DAY;
    }

    public Map<String, Object> getStudentSummary(String studentId) {
        if (studentId == null || studentId.trim().isEmpty())
            throw new IllegalArgumentException("Student ID cannot be null or empty");

        studentId = studentId.trim();
        Map<String, Object> summary = new HashMap<>();

        summary.put("student", students.get(studentId));

        List<BorrowRecord> allRecords = borrowRecords.getRecordsByStudent(studentId);
        List<BorrowRecord> activeRecords = new ArrayList<>();
        List<BorrowRecord> overdueRecords = new ArrayList<>();
        double totalFine = 0.0;

        for (BorrowRecord r : allRecords) {
            if (!r.isReturned())
                activeRecords.add(r);
            if (r.isOverdue()) {
                overdueRecords.add(r);
                long daysLate = ChronoUnit.DAYS.between(r.getExpectedReturnDate(), LocalDate.now());
                totalFine += daysLate * FINE_PER_DAY;
            }
        }

        summary.put("totalBorrows", allRecords.size());
        summary.put("activeBorrows", activeRecords);
        summary.put("activeBorrowCount", activeRecords.size());
        summary.put("overdueBooks", overdueRecords);
        summary.put("overdueCount", overdueRecords.size());
        summary.put("totalFine", totalFine);
        summary.put("canBorrow", activeRecords.size() < MAX_BORROW_LIMIT);

        return summary;
    }

    public void addCopiesToBook(String isbn, int count) {
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        if (count <= 0)
            throw new IllegalArgumentException("Count must be greater than zero");

        Book book = books.search(isbn.trim());
        if (book == null)
            throw new IllegalArgumentException("Book not found: " + isbn);

        book.addCopies(count);
        processWaitlistForBook(book);
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    public List<Student> getStudentsWithActiveBorrows() {
        List<BorrowRecord> active = borrowRecords.getActiveRecords();
        List<Student> result = new ArrayList<>();
        List<String> seen = new ArrayList<>();
        for (BorrowRecord r : active) {
            if (!seen.contains(r.getStudentId())) {
                seen.add(r.getStudentId());
                Student s = students.get(r.getStudentId());
                if (s != null)
                    result.add(s);
            }
        }
        return result;
    }

    public List<Student> getStudentsWithOverdueBooks() {
        List<BorrowRecord> overdue = borrowRecords.getOverdueRecords();
        List<Student> result = new ArrayList<>();
        List<String> seen = new ArrayList<>();
        for (BorrowRecord r : overdue) {
            if (!seen.contains(r.getStudentId())) {
                seen.add(r.getStudentId());
                Student s = students.get(r.getStudentId());
                if (s != null)
                    result.add(s);
            }
        }
        return result;
    }

    public Map<String, Object> getLibraryStats(List<Book> allBooks) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalStudents", students.size());
        stats.put("totalBorrowRecords", borrowRecords.size());
        stats.put("activeBorrows", borrowRecords.getActiveRecords().size());
        stats.put("overdueCount", borrowRecords.getOverdueRecords().size());
        stats.put("waitlistSize", waitlist.size());
        if (allBooks != null)
            stats.put("mostBorrowedBook", getMostBorrowedBook(allBooks));
        return stats;
    }
}