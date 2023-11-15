import Utilities.Code;

import java.time.LocalDate;
import java.util.*;

/**
 * Represents the fourth class in Project 1 (Part 04/04) and represents a library.
 *
 * @author Nicole Al-Sabah
 * Date: November 12, 2023
 */
public class Library {
  public final static int LENDING_LIMIT = 5;

  private String name;
  private static int libraryCard;
  private List<Reader> readers;
  private HashMap<String, Shelf> shelves = new HashMap<>();
  private HashMap<Book, Integer> books = new HashMap<>();

  public Library(String name) {
      this.name = name;
  }

  public Code init(String filename) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
  }

  private Code initBooks(int bookCount, Scanner scan) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
  }

  private Code initShelves(int shelfCount, Scanner scan) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
  }

  private Code initReader(int readerCount, Scanner scan) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Code addBook(Book newBook) {
      if (books.containsKey(newBook)) {
          int count = books.get(newBook);
          count++;
          books.put(newBook, count);
          System.out.println(count + " copies of " + newBook.getTitle() + " in the stacks");
      } else {
          books.put(newBook, 1);
          System.out.println(newBook.getTitle() + " added to the stacks.");
      }

      Shelf shelf = getShelf(newBook.getSubject());

      if (shelf != null) {
          addBookToShelf(newBook, shelf);
          return Code.SUCCESS;
      } else {
          System.out.println("No shelf for " + newBook.getSubject() + " books");
          return Code.SHELF_EXISTS_ERROR;
      }
  }

  public Code returnBook(Reader reader, Book book) {
      if (!reader.hasBook(book)) {
          System.out.println(reader.getName() + " doesn't have " + book + " checked out");
          return Code.READER_DOESNT_HAVE_BOOK_ERROR;
      }

      if (!books.containsKey(book)) {
          System.out.println(reader.getName() + " is returning " + book);
          reader.removeBook(book);
          return Code.BOOK_NOT_IN_INVENTORY_ERROR;
      }

      if (reader.removeBook(book) == Code.SUCCESS) {
          returnBook(book);
          return Code.SUCCESS;
      } else {
          System.out.println("Could not return " + book);
          return Code.READER_COULD_NOT_REMOVE_BOOK_ERROR;
      }
  }

  public Code returnBook(Book book) {
      if (shelves.containsKey(book.getSubject())) {
          Shelf shelf = shelves.get(book.getSubject());
          shelf.addBook(book);
          return Code.SUCCESS;
      } else {
          System.out.println("No shelf for " + book);
          return Code.SHELF_EXISTS_ERROR;
      }
  }

  private Code addBookToShelf(Book book, Shelf shelf) {
      if (returnBook(book) == Code.SUCCESS) {
          return Code.SUCCESS;
      }

      if (!shelves.containsKey(book.getSubject())) {
          return Code.SHELF_SUBJECT_MISMATCH_ERROR;
      }

      if (shelf.addBook(book) == Code.SUCCESS) {
          System.out.println(book + " added to shelf");
          return Code.SUCCESS;
      } else {
          System.out.println("Could not add " + book + " to shelf");
          return Code.SHELF_SUBJECT_MISMATCH_ERROR;
      }
  }

  public int listBooks() {
      System.out.println("Not implemented");
      return 0;
  }

  public Code checkOutBook(Reader reader, Book book) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
  }
  public Book getBookByISBN(String isbn) {
      List<Book> listBooks = new ArrayList<>(books.keySet());

      for (int i = 0; i < listBooks.size(); i++) {
          Book book = listBooks.get(i);

          if (book.getISBN().equals(isbn)) {
              return book;
          }
      }

      System.out.println("ERROR: Could not find a book with isbn: " + isbn);
      return null;
  }

  public int listShelves() {
      return listShelves(false);
  }

  public int listShelves(boolean showBooks) {
      int shelfCount = 0;

      Set<String> shelfSet = shelves.keySet();
      List<String> listShelf = new ArrayList<>(shelfSet);

        for (int i = 0; i < listShelf.size(); i++) {
            String shelfSubject = listShelf.get(i);
            Shelf shelf = shelves.get(shelfSubject);

            if (showBooks) {
                shelf.listBooks();
            } else {
                System.out.println(shelf.toString());
            }

            shelfCount++;
        }

      return shelfCount;
  }

  public Code addShelf(String shelfSubject) {
      Shelf newShelf = new Shelf(shelves.size() + 1, shelfSubject);

      return addShelf(newShelf);
  }

  public Code addShelf(Shelf shelf) {
      if (shelves.containsKey(shelf.getSubject())) {
          System.out.println("ERROR: Shelf already exists " + shelf);
          return Code.SHELF_EXISTS_ERROR;
      }

      shelves.put(shelf.getSubject(), shelf);

      int maxNumber = 0;
      List<Shelf> listShelf = new ArrayList<>(shelves.values());

      for (int i = 0; i < listShelf.size(); i++) {
          Shelf shelfSize = listShelf.get(i);
          maxNumber = Math.max(maxNumber, shelfSize.getShelfNumber());
      }

      shelf.setShelfNumber(maxNumber + 1);

      List<Book> listBooks = new ArrayList<>(books.keySet());

      for (int i = 0; i < listBooks.size(); i++) {
          Book book = listBooks.get(i);
          if (book.getSubject().equals(shelf.getSubject())) {
              addBookToShelf(book, shelf);
          }
      }

      return Code.SUCCESS;
  }

  public Shelf getShelf(Integer shelfNumber) {
      System.out.println("Not implemented");
      return null;
  }

  public Shelf getShelf(String subject) {
      System.out.println("Not implemented");
      return null;
  }

  public int listReaders() {
      System.out.println("Not implemented");
      return 0;
  }

  public int listReaders(boolean showBooks) {
      System.out.println("Not implemented");
      return 0;
  }

  public Reader getReaderByCard(int cardNumber) {
      System.out.println("Not implemented");
      return null;
  }

  public Code addReader(Reader reader) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Code removeReader(Reader reader) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
  }

  public static int convertInt(String recordCountString, Code code) {
      System.out.println("Not implemented");
      return 0;
  }

  public static LocalDate convertDate(String date, Code errorCode) {
      System.out.println("Not implemented");
      return null;
  }

  public static int getLibraryCardNumber() {
      return libraryCard + 1;
  }

  private Code errorCode(int codeNumber) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
  }

  public String getName() {
      return name;
  }
}
