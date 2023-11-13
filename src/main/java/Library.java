import Utilities.Code;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

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
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
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
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
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
      System.out.println("Not implemented");
      return null;
  }

  public int listShelves() {
      return listShelves(false);
  }

  public int listShelves(boolean showBooks) {
      System.out.println("Not implemented");
      return 0;
  }

  public Code addShelf(String shelfSubject) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
  }

  public Code addShelf(Shelf shelf) {
      System.out.println("Not implemented");
      return Code.NOT_IMPLEMENTED_ERROR;
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
