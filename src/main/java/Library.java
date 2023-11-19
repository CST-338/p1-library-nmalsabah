import Utilities.Code;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * Represents the fourth class in Project 1 (Part 04/04) and represents a library.
 *
 * @author Nicole Al-Sabah
 * Date: November 12, 2023
 */
public class Library {
  public final static int LENDING_LIMIT = 5;

  private final String name;
  private static int libraryCard;
  private final List<Reader> readers = new ArrayList<>();
  private final HashMap<String, Shelf> shelves = new HashMap<>();
  private final HashMap<Book, Integer> books = new HashMap<>();

  public Library(String name) {
      this.name = name;
  }

  public Code init(String filename) {
      File file = new File(filename);

      if (!file.exists()) {
          return Code.FILE_NOT_FOUND_ERROR;
      }

      try {
          Scanner scanner = new Scanner(file);
          // Books
          String bookNumberLine = scanner.nextLine();
          int numberOfBooks = convertInt(bookNumberLine, Code.BOOK_COUNT_ERROR);
          if (numberOfBooks < 0) {
                return errorCode(numberOfBooks);
          }

          Code initBooksCode = initBooks(numberOfBooks, scanner);

          if (initBooksCode != Code.SUCCESS) {
              return initBooksCode;
          }

          // Shelves
          String shelfNumberLine = scanner.nextLine();
          int numberOfShelves = convertInt(shelfNumberLine, Code.SHELF_COUNT_ERROR);
          if (numberOfShelves < 0) {
              return errorCode(numberOfShelves);
          }

          Code initShelvesCode = initShelves(numberOfShelves, scanner);

          if (initShelvesCode != Code.SUCCESS) {
              return initShelvesCode;
          }

          // Readers
          String readerNumberLine = scanner.nextLine();
          int numberOfReaders = convertInt(readerNumberLine, Code.READER_COUNT_ERROR);
          if (numberOfReaders < 0) {
              return errorCode(numberOfReaders);
          }

          Code initReaderCode = initReader(numberOfReaders, scanner);

          if (initReaderCode != Code.SUCCESS) {
              return initReaderCode;
          }
      } catch (Exception e) {
      }

      return Code.SUCCESS;
  }

  private Code initBooks(int bookCount, Scanner scan) {
      for (int i = 0; i < bookCount; i++) {
          try {
              String bookLine = scan.nextLine();
              String[] bookInfo = bookLine.split(",");

              if (bookInfo.length != 6) {
                  return Code.BOOK_COUNT_ERROR;
              }

              String isbn = bookInfo[0];
              String title = bookInfo[1];
              String subject = bookInfo[2];
              int pageCount = convertInt(bookInfo[3], Code.PAGE_COUNT_ERROR);
              String author = bookInfo[4];
              LocalDate dueDate = convertDate(bookInfo[5], Code.DATE_CONVERSION_ERROR);
              Book book = new Book(isbn, title, subject, pageCount, author, dueDate);
              addBook(book);
          } catch (Exception e) {
              return Code.BOOK_COUNT_ERROR;
          }
      }

      return Code.SUCCESS;
  }

  private Code initShelves(int shelfCount, Scanner scan) {
      for (int i = 0; i < shelfCount; i++) {
          try {
              String shelfLine = scan.nextLine();
              String[] shelfInfo = shelfLine.split(",");

              if (shelfInfo.length != 2) {
                  return Code.SHELF_NUMBER_PARSE_ERROR;
              }

              int shelfNumber = convertInt(shelfInfo[0], Code.SHELF_COUNT_ERROR);

              if (shelfNumber < 1) {
                    return Code.SHELF_NUMBER_PARSE_ERROR;
              }

              String subject = shelfInfo[1];
              Shelf shelf = new Shelf(shelfNumber, subject);
              addShelf(shelf);
          } catch (Exception e) {
              System.out.println("Number of shelves doesn't match expected");
              return Code.SHELF_NUMBER_PARSE_ERROR;
          }
      }

      return Code.SUCCESS;
  }

  private Code initReader(int readerCount, Scanner scan) {
      for (int i = 0; i < readerCount; i++) {
          try {
              String readerLine = scan.nextLine();
              String[] readerInfo = readerLine.split(",");

              int cardNumber = convertInt(readerInfo[0], Code.READER_CARD_NUMBER_ERROR);
              String name = readerInfo[1];
              String phone = readerInfo[2];
              Reader reader = new Reader(cardNumber, name, phone);
              addReader(reader);

              String numberOfBooks = readerInfo[3];
              int bookCount = convertInt(numberOfBooks, Code.BOOK_COUNT_ERROR);

              if (readerInfo.length != 4 + bookCount * 2) {
                  return Code.READER_COUNT_ERROR;
              }

              for (int j = 0; j < bookCount; j+= 2) {
                  String isbn = readerInfo[4 + j];
                  String dueDate = readerInfo[5 + j];
                  Book book = getBookByISBN(isbn);

                  if (book == null) {
                      System.out.println("Error");
                      continue;
                  }

                  checkOutBook(reader, book);
                  book.setDueDate(convertDate(dueDate, Code.DATE_CONVERSION_ERROR));
              }

          } catch (Exception e) {
              return Code.SHELF_NUMBER_PARSE_ERROR;
          }
      }

      return Code.SUCCESS;
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
      Set<Book> bookSet = books.keySet();

      ArrayList<Book> bookList = new ArrayList<>(bookSet);

      int bookCount = 0;

      for (int i = 0; i < bookList.size(); i++) {
          Book book = bookList.get(i);
          int count = books.get(book);
          bookCount += count;
          System.out.println(count + " copies of " + bookList.get(i));
      }

      return bookCount;
  }

  public Code checkOutBook(Reader reader, Book book) {
      if (!readers.contains(reader)) {
          System.out.println(reader.getName() + " doesn't have an account here");
          return Code.READER_NOT_IN_LIBRARY_ERROR;
      }

      if (reader.getBookCount() >= LENDING_LIMIT) {
          System.out.println(reader.getName() + " has reached the lending limit, " + "(" + LENDING_LIMIT + ")");
          return Code.BOOK_LIMIT_REACHED_ERROR;
      }

      if (!books.containsKey(book)) {
          System.out.println("ERROR: could not find " + book);
          return Code.BOOK_NOT_IN_INVENTORY_ERROR;
      }

      if (!shelves.containsKey(book.getSubject())) {
          System.out.println("no shelf for " + book.getSubject() + " books!");
          return Code.SHELF_EXISTS_ERROR;
      }

      if (shelves.get(book.getSubject()).getBookCount(book) < 1) {
          System.out.println("ERROR: no copies of " + book + " remain");
          return Code.BOOK_NOT_IN_INVENTORY_ERROR;
      }

      if (reader.addBook(book) != Code.SUCCESS) {
          System.out.println("Couldn't checkout " + book);
          return Code.SHELF_EXISTS_ERROR;
      }

      if (shelves.get(book.getSubject()).removeBook(book) == Code.SUCCESS) {
          System.out.println(book + " checked out successfully");
          return Code.SUCCESS;
      }

      return Code.SUCCESS;
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
      // Citation for Math.max
      // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Math.html
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
      if (shelves.containsKey(shelfNumber.toString())) {
          return shelves.get(shelfNumber.toString());
      } else {
          System.out.println("No shelf number " + shelfNumber + " found");
          return null;
      }
  }

  public Shelf getShelf(String subject) {
        if (shelves.containsKey(subject)) {
            return shelves.get(subject);
        } else {
            System.out.println("No shelf for " + subject + " books");
            return null;
        }
  }

  public int listReaders() {
      for (int i = 0; i < readers.size(); i++) {
            Reader reader = readers.get(i);
            System.out.println(reader.toString());
      }

      return readers.size();
  }

  public int listReaders(boolean showBooks) {
      for (int i = 0; i < readers.size(); i++) {
          Reader reader = readers.get(i);

          if (showBooks) {
              System.out.println(reader.getName() + " (#" + reader.getCardNumber() + ")" + " has the following books:" + "\n" + reader.getBooks());
          } else {
              System.out.println(reader.toString());
          }
      }

      return readers.size();
  }

  public Reader getReaderByCard(int cardNumber) {
      for (int i = 0; i < readers.size(); i++) {
          Reader reader = readers.get(i);
          if (reader.getCardNumber() == cardNumber) {
              return reader;
          }
      }

      System.out.println("Could not find a reader with card #" + cardNumber);
      return null;
  }

  public Code addReader(Reader reader) {
      if (readers.contains(reader)) {
          System.out.println(reader.getName() + " already has an account!");
          return Code.READER_ALREADY_EXISTS_ERROR;
      }

      for (int i = 0; i < readers.size(); i++) {
          Reader existingReader = readers.get(i);
          if (existingReader.getCardNumber() == reader.getCardNumber()) {
              System.out.println(existingReader.getName() + " and " + reader.getName() + " have the same card number!");
              return Code.READER_CARD_NUMBER_ERROR;
          }
      }

      readers.add(reader);
      System.out.println(reader.getName() + " added to the library!");

      if (reader.getCardNumber() > libraryCard) {
          libraryCard = reader.getCardNumber();
      }

      return Code.SUCCESS;
  }

  public Code removeReader(Reader reader) {
      if (!readers.contains(reader)) {
          System.out.println(reader.getName() + " is not part of this Library");
          return Code.READER_NOT_IN_LIBRARY_ERROR;
      }

      if (reader.getBookCount() > 0) {
          System.out.println(reader.getName() + " must return all books!");
          return Code.READER_STILL_HAS_BOOKS_ERROR;
      }

      readers.remove(reader);
      return Code.SUCCESS;
  }

  public static int convertInt(String recordCountString, Code code) {
      try {
          return Integer.parseInt(recordCountString);
      } catch (NumberFormatException e) {
          switch (code) {
              case BOOK_COUNT_ERROR:
                  System.out.println("Error: Could not read number of books");
                  break;
              case PAGE_COUNT_ERROR:
                  System.out.println("Error: could not parse page count");
                  break;
              case DATE_CONVERSION_ERROR:
                  System.out.println("Error: Could not parse date component");
                  break;
              default:
                  System.out.println("Error: Unknown conversion error");
                  break;
          }
          return code.getCode();
      }
  }

  public static LocalDate convertDate(String date, Code errorCode) {
      LocalDate defaultDate = LocalDate.of(1970, 1, 1);

      if (Objects.equals(date, "0000")) {
          return defaultDate;
      }
      try {
          return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      } catch (DateTimeParseException e) {
          System.out.println("ERROR: date conversion error, could not parse " + date);
          System.out.println("Using default date (01-jan-1970)");
          return defaultDate;
      }
  }

  public static int getLibraryCardNumber() {
      return libraryCard + 1;
  }

  private Code errorCode(int codeNumber) {
      for (Code code : Code.values()) {
          if (code.getCode() == codeNumber) {
              return code;
          }
      }

      return Code.UNKNOWN_ERROR;
  }

  public String getName() {
      return name;
  }
}
