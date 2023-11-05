import Utilities.Code;

import java.util.HashMap;
import java.util.Objects;

/**
 * Represents the third class in Project 1 (Part 03/04) and serves as a shelf for storing books.
 *
 * @author Nicole Al-Sabah
 * Date: November 5, 2023
 */
public class Shelf {
    public final static int SHELF_NUMBER_ = 0;
    public final static int SUBJECT_ = 1;

    private HashMap<Book, Integer> books = new HashMap<>();
    private int shelfNumber;
    private String subject;

    public Shelf() {
    }

    public Shelf(int shelfNumber, String subject) {
        this.shelfNumber = shelfNumber;
        this.subject = subject;
    }

    public Code addBook(Book book) {
        // Citation for containsKey(), get(), and put()
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/HashMap.html
        if (books.containsKey(book)) {
            int count = books.get(book);
            books.put(book, count + 1);

            return Code.SUCCESS;
        }

        // Citation for researching "this" to print toString()
        // https://stackoverflow.com/questions/59557088/why-does-the-this-keyword-print-exactly-what-tostring-is-printing
        if (subject.equals(book.getSubject())) {
            books.put(book, 1);
            System.out.println(book + " added to shelf " + this);

            return Code.SUCCESS;
        } else {
            return Code.SHELF_SUBJECT_MISMATCH_ERROR;
        }
    }

    public Code removeBook(Book book) {
        if (!books.containsKey(book)) {
            System.out.println(book + " is not on shelf " + subject);

            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        }

        if (books.containsValue(0)) {
            System.out.println("No copies of " + book + " remain on shelf " + subject);
            return Code.BOOK_NOT_IN_INVENTORY_ERROR;
        } else {
            int count = books.get(book);
            books.put(book, count - 1);
            System.out.println(book + " successfully removed from shelf " + subject);

            return Code.SUCCESS;
        }
    }

    public String listBooks() {
        return books.size() + " book on shelf: " + this + "\n";
    }

    @Override
    public String toString() {
        return shelfNumber + " : " + subject;
    }

    public int getBookCount(Book book) {
        Integer count = books.get(book);

        // Citing requireNonNullElse()
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Objects.html
        return Objects.requireNonNullElse(count, -1);
    }

    public HashMap<Book, Integer> getBooks() {
        return books;
    }

    public void setBooks(HashMap<Book, Integer> books) {
        this.books = books;
    }

    public int getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfNumber(int shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        if (getShelfNumber() != shelf.getShelfNumber()) return false;
        return getSubject() != null ? getSubject().equals(shelf.getSubject()) : shelf.getSubject() == null;
    }

    @Override
    public int hashCode() {
        int result = getShelfNumber();
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        return result;
    }
}
