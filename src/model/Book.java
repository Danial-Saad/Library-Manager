package model;

public class Book {
    private String isbn;
    private String title;
    private String authorName;
    private int totalCopies;
    private int availableCopies;

    public Book(String isbn, String title, String authorName, int totalCopies) {
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be null or empty");
        if (authorName == null || authorName.trim().isEmpty())
            throw new IllegalArgumentException("Author name cannot be null or empty");
        if (totalCopies <= 0)
            throw new IllegalArgumentException("Total copies must be greater than zero");

        this.isbn = isbn.trim();
        this.title = title.trim();
        this.authorName = authorName.trim();
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
    }

    public boolean isAvailable() {
        return availableCopies > 0;
    }

    public boolean borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }

    public void returnCopy() {
        if (availableCopies >= totalCopies)
            throw new IllegalStateException("Cannot return copy: no copies are currently borrowed for book [" + isbn + "]");
        availableCopies++;
    }

    public void addCopies(int count) {
        if (count <= 0)
            throw new IllegalArgumentException("Count must be greater than zero");
        totalCopies += count;
        availableCopies += count;
    }

    public String getIsbn() { return isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title cannot be null or empty");
        this.title = title.trim();
    }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) {
        if (authorName == null || authorName.trim().isEmpty())
            throw new IllegalArgumentException("Author name cannot be null or empty");
        this.authorName = authorName.trim();
    }

    public int getTotalCopies() { return totalCopies; }
    public void setTotalCopies(int totalCopies) {
        if (totalCopies < getBorrowedCopies())
            throw new IllegalArgumentException("Total copies cannot be less than currently borrowed copies");
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(int availableCopies) {
        if (availableCopies < 0 || availableCopies > totalCopies)
            throw new IllegalArgumentException("Available copies must be between 0 and total copies");
        this.availableCopies = availableCopies;
    }

    public int getBorrowedCopies() { return totalCopies - availableCopies; }

    @Override
    public String toString() {
        return "[" + isbn + "] " + title + " by " + authorName +
               " | Available: " + availableCopies + "/" + totalCopies;
    }
}
