import Utilities.Code;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the second class in Project 1 (Part 02/04), responsible for interacting with books in a library.
 *
 * @author Nicole Al-Sabah
 * Date: November 4, 2023
 */
public class Reader {
    public final static int CARD_NUMBER_ = 0;
    public final static int NAME_ = 1;
    public final static int PHONE_ = 2;
    public final static int BOOK_COUNT_ = 3;
    public final static int BOOK_START_ = 4;

    private int cardNumber;
    private String name;
    private String phone;
    private List<Book> books;

    public Reader(int cardNumber, String name, String phone) {
        this.cardNumber = cardNumber;
        this.name = name;
        this.phone = phone;

        books = new ArrayList<>();
    }

    public Code addBook(Book book){
        // Adding citation for add()
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/List.html
        if (books.contains(book)){
            return Code.BOOK_ALREADY_CHECKED_OUT_ERROR;
        }
        books.add(book);
        return Code.SUCCESS;
    }

    public Code removeBook(Book book) {
        // Adding citation for remove()
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/List.html
        if (!books.contains(book)) {
            books.remove(book);
            return Code.READER_DOESNT_HAVE_BOOK_ERROR;
        }

        if (books.remove(book)) {
            return Code.SUCCESS;
        } else {
            return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
        }
    }

    public boolean hasBook(Book book) {
        return books.contains(book);
    }

    @Override
    public String toString() {
        return name + " (#" + cardNumber + ")" + " has checked out " + books;
    }

    public int getBookCount() {
        return books.size();
    }

    public int getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Reader reader = (Reader) o;

        if (cardNumber != reader.cardNumber) return false;
        if (!Objects.equals(name, reader.name)) return false;
        return Objects.equals(phone, reader.phone);
    }

    @Override
    public int hashCode() {
        int result = cardNumber;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        return result;
    }
}
