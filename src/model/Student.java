package model;

public class Student {
    private String id;
    private String name;
    private boolean isGraduated;

    public Student(String id, String name, boolean isGraduated) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("Student ID cannot be null or empty");
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Student name cannot be null or empty");
        this.id = id.trim();
        this.name = name.trim();
        this.isGraduated = isGraduated;
    }

    public String getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Student name cannot be null or empty");
        this.name = name.trim();
    }

    public boolean isGraduated() { return isGraduated; }
    public void setGraduated(boolean graduated) { isGraduated = graduated; }

    public int getPriority() {
        return isGraduated ? 1 : 2;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + name + (isGraduated ? " (Graduated)" : " (Student)");
    }
}
