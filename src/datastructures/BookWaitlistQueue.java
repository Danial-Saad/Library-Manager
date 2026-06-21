package datastructures;

import model.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookWaitlistQueue {
    private List<WaitlistEntry> heap;

    public BookWaitlistQueue() {
        this.heap = new ArrayList<>();
    }

    private int parent(int i) {
        return (i - 1) / 2;
    }

    private int leftChild(int i) {
        return 2 * i + 1;
    }

    private int rightChild(int i) {
        return 2 * i + 2;
    }

    private boolean hasHigherPriority(WaitlistEntry a, WaitlistEntry b) {
        // نستخدم دالة compareTo الموجودة في WaitlistEntry
        // إذا رجعت قيمة سالبة فهذا يعني أن 'a' له أولوية أعلى (أو أقدم زمناً)
        return a.compareTo(b) < 0;
    }

    private void swap(int i, int j) {
        WaitlistEntry temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    private void heapifyUp(int index) {
        while (index > 0 && hasHigherPriority(heap.get(index), heap.get(parent(index)))) {
            swap(index, parent(index));
            index = parent(index);
        }
    }

    private void heapifyDown(int index) {
        int size = heap.size();
        int highest = index;
        int left = leftChild(index);
        int right = rightChild(index);

        if (left < size && hasHigherPriority(heap.get(left), heap.get(highest)))
            highest = left;
        if (right < size && hasHigherPriority(heap.get(right), heap.get(highest)))
            highest = right;

        if (highest != index) {
            swap(index, highest);
            heapifyDown(highest);
        }
    }

    public void enqueue(Student student, String isbn) {
        if (student == null || isbn == null || isbn.trim().isEmpty())
            return;
        isbn = isbn.trim();

        if (isStudentWaiting(student.getId(), isbn))
            return;

        WaitlistEntry entry = new WaitlistEntry(student, isbn);
        heap.add(entry);
        heapifyUp(heap.size() - 1);
    }

    public WaitlistEntry dequeue() {
        if (heap.isEmpty())
            return null;

        WaitlistEntry top = heap.get(0);
        WaitlistEntry last = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        return top;
    }

    public WaitlistEntry peek() {
        return heap.isEmpty() ? null : heap.get(0);
    }

    public boolean removeStudent(String studentId, String isbn) {
        if (studentId == null || studentId.trim().isEmpty() || isbn == null || isbn.trim().isEmpty())
            return false;

        studentId = studentId.trim();
        isbn = isbn.trim();

        for (int i = 0; i < heap.size(); i++) {
            WaitlistEntry e = heap.get(i);
            if (e.getStudent().getId().equals(studentId) && e.getIsbn().equals(isbn)) {
                heap.remove(i);
                // إعادة بناء الكومة بالكامل لضمان التوازن بعد الحذف من المنتصف
                for (int j = heap.size() / 2 - 1; j >= 0; j--) {
                    heapifyDown(j);
                }
                return true;
            }
        }
        return false;
    }

    public boolean isStudentWaiting(String studentId, String isbn) {
        if (studentId == null || studentId.trim().isEmpty() || isbn == null || isbn.trim().isEmpty())
            return false;

        studentId = studentId.trim();
        isbn = isbn.trim();

        for (WaitlistEntry e : heap) {
            if (e.getStudent().getId().equals(studentId) && e.getIsbn().equals(isbn)) {
                return true;
            }
        }
        return false;
    }

    public List<WaitlistEntry> getWaitlistForBook(String isbn) {
        List<WaitlistEntry> result = new ArrayList<>();
        if (isbn == null || isbn.trim().isEmpty())
            return result;

        isbn = isbn.trim();
        for (WaitlistEntry e : heap) {
            if (e.getIsbn().equals(isbn)) {
                result.add(e);
            }
        }
        Collections.sort(result); // ترتيب النتائج للعرض
        return result;
    }

    public List<WaitlistEntry> getAllEntries() {
        List<WaitlistEntry> result = new ArrayList<>(heap);
        Collections.sort(result); // ترتيب النتائج للعرض
        return result;
    }

    public int size() {
        return heap.size();
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }
}