package datastructures;

import model.Student;

public class WaitlistEntry implements Comparable<WaitlistEntry> {
    Student student;
    String isbn;
    int priority;
    long timestamp;

    public WaitlistEntry(Student student, String isbn) {
        if (student == null)
            throw new IllegalArgumentException("Student cannot be null");
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN cannot be null or empty");

        this.student = student;
        this.isbn = isbn.trim();
        this.priority = student.getPriority();
        this.timestamp = System.nanoTime();
    }

    public Student getStudent() {
        return student;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getPriority() {
        return priority;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public int compareTo(WaitlistEntry other) {
        if (this.priority != other.priority) {
            return Integer.compare(this.priority, other.priority);
        }
        return Long.compare(this.timestamp, other.timestamp);
    }
}