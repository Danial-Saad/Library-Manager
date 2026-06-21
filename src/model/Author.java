package model;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private String name;
    private String nationality;
    private List<String> bookIsbns;

    public Author(String name, String nationality) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Author name cannot be null or empty");
        this.name = name.trim();
        this.nationality = (nationality != null) ? nationality.trim() : "";
        this.bookIsbns = new ArrayList<>();
    }

    public void addBookIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        if (!bookIsbns.contains(isbn.trim()))
            bookIsbns.add(isbn.trim());
    }

    public void removeBookIsbn(String isbn) {
        if (isbn == null || isbn.trim().isEmpty())
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        bookIsbns.remove(isbn.trim());
    }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Author name cannot be null or empty");
        this.name = name.trim();
    }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) {
        this.nationality = (nationality != null) ? nationality.trim() : "";
    }

    public List<String> getBookIsbns() { return new ArrayList<>(bookIsbns); }

    public int getBookCount() { return bookIsbns.size(); }

    @Override
    public String toString() {
        return name + " (" + nationality + ") - " + bookIsbns.size() + " book(s)";
    }
}
