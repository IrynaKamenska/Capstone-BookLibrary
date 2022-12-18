package de.neuefische.booklibrary.backend.book;

import de.neuefische.booklibrary.backend.api.Isbn;
import de.neuefische.booklibrary.backend.security.AppUserRepository;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.mongodb.assertions.Assertions.fail;
import static de.neuefische.booklibrary.backend.book.Availability.AVAILABLE;
import static de.neuefische.booklibrary.backend.book.Availability.NOT_AVAILABLE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private final BookRepository bookRepository = mock(BookRepository.class);
    private final AppUserRepository appUserRepository = mock(AppUserRepository.class);
    private final BookService bookService = new BookService(bookRepository, appUserRepository);

    private final RentBookInfo rentBookInfoEmpty = new RentBookInfo("", "");
    private final Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
    private final Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
    private final List<Isbn> isbns = new ArrayList<>(List.of(isbn_13, isbn_10));
    private final String category = "Fiction";
    private final String printType = "BOOK";
    private final int pageCount = 100;

    @Test
    void getAllBooks_ifNoBooksExist_returnEmptyList() {
        //given
        List<Book> books = new ArrayList<>();
        when(bookRepository.findAll()).thenReturn(books);

        //when
        List<Book> actual = bookService.getAllBooks();

        //then
        verify(bookRepository).findAll();
        assertEquals(books, actual);

    }


    @Test
    void getAllBooks_returnListWithOneBook() {
        //given
        Book book = new Book("id1", null, "Java", "M. Kofler", List.of(isbn_10), category, printType, pageCount, AVAILABLE, rentBookInfoEmpty);
        Book foundBook = book.withTitle("Java").withAuthor("M. Kofler").withIsbn(List.of(isbn_10)).withAvailability(AVAILABLE);

        when(bookRepository.findAll()).thenReturn(List.of(foundBook));

        //when
        List<Book> expected = List.of(foundBook);
        List<Book> actual = bookService.getAllBooks();

        //then
        verify(bookRepository).findAll();
        assertEquals(expected, actual);

    }


    @Test
    void addNewBookWithoutId_returnBookWithId() {
        //given
        Book book = new Book(null, null, "Java-Script", "P. Ackermann", List.of(isbn_13), category, printType, pageCount, NOT_AVAILABLE, rentBookInfoEmpty);
        Book saveBook = book.withTitle("Java-Script").withAuthor("P. Ackermann").withIsbn(List.of(isbn_13)).withAvailability(NOT_AVAILABLE);
        when(bookRepository.save(saveBook)).thenReturn(saveBook.withId("id1"));

        //when
        Book actual = bookService.saveBook(saveBook);
        Book expected = saveBook.withId("id1");

        //then
        assertEquals(expected, actual);
    }

    @Test
    void addNewBookWithId_returnBook() {
        //given
        Book book = new Book("id1", null, "Java-Script", "P. Ackermann", List.of(isbn_13), category, printType, pageCount, NOT_AVAILABLE, rentBookInfoEmpty);
        Book saveBook = book.withId("id1").withTitle("Java-Script").withAuthor("P. Ackermann").withIsbn(List.of(isbn_13)).withAvailability(NOT_AVAILABLE);
        when(bookRepository.save(saveBook)).thenReturn(saveBook);

        //when
        Book actual = bookService.saveBook(saveBook);

        //then
        assertEquals(saveBook, actual);
    }


    @Test
    void updateBookById_returnUpdatedBook() {
        //given
        Book book = new Book("id1", null, "Java-Script", "P. Ackermann", List.of(isbn_13), category, printType, pageCount, NOT_AVAILABLE, rentBookInfoEmpty);
        Book toUpdateBook = book.withId("id1").withTitle("Java-Script").withAuthor("P. Ackermann").withIsbn(List.of(isbn_13)).withAvailability(NOT_AVAILABLE);

        when(bookRepository.save(toUpdateBook)).thenReturn(toUpdateBook);

        //when
        Book actual = bookService.updateBook(toUpdateBook);

        //then
        verify(bookRepository).save(toUpdateBook);
        assertEquals(toUpdateBook, actual);
    }

    @Test
    void isBookExisting_returnTrue() {
        //given
        String id = "id1";
        when(bookRepository.existsById(id)).thenReturn(true);

        //when
        boolean actual = bookService.isBookExisting(id);
        boolean expected = true;

        //then
        assertEquals(expected, actual);
    }

    @Test
    void isBookDoesNotExisting_returnFalse() {
        //given
        String id = "id1";
        when(bookRepository.existsById(id)).thenReturn(false);

        //when
        boolean actual = bookService.isBookExisting(id);

        //then
        assertFalse(actual);
    }

    @Test
    void deleteBookById() {
        Book book = new Book("id1", null, "Java-Script", "P. Ackermann", List.of(isbn_13), category, printType, pageCount, NOT_AVAILABLE, rentBookInfoEmpty);
        doNothing().when(bookRepository).deleteById(book.id());
        //when
        bookService.deleteBook(book.id());
        //then
        verify(bookRepository).deleteById(book.id());
    }

    @Test
    void rentBookById_returnBookWithUpdatedRentBookInfo() {
        //given
        String bookId = "id-1";
        String username = "username";
        Book book = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                List.of(isbn_13), category, printType, pageCount,
                AVAILABLE,
                rentBookInfoEmpty);

        RentBookInfo rentBookInfo = new RentBookInfo(username, "2022-12-14");
        Book bookToRent = book.withId(bookId)
                .withAvailability(NOT_AVAILABLE)
                .withRentBookInfo(rentBookInfo);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(appUserRepository.existsByUsername(username)).thenReturn(true);
        when(bookRepository.save(bookToRent)).thenReturn(bookToRent);
        Book actual = bookService.rentBook(bookId, rentBookInfo);

        //then
        verify(bookRepository).save(bookToRent);
        assertEquals(bookToRent, actual);
    }


    @Test
    void rentBookWithNotExistingBookId_returnNoSuchElementException() {
        //given
        String bookId = "id-1";
        String username = "username";
        Book book = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                List.of(isbn_13), category, printType, pageCount,
                AVAILABLE,
                rentBookInfoEmpty);
        RentBookInfo rentBookInfo = new RentBookInfo(username, "2022-12-14");
        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        try {
            bookService.rentBook(bookId, rentBookInfo);
            fail();
        } catch (NoSuchElementException e) {
            //then
            assertEquals("No Book found with this ID", e.getMessage());
            verify(bookRepository, never()).save(book);
        }
    }


    @Test
    void rentNotAvailableBook_returnBookNotAvailableException() {
        //given
        String bookId = "id-1";
        String appUsername = "username";
        Book foundBook = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                List.of(isbn_13), category, printType, pageCount,
                NOT_AVAILABLE,
                rentBookInfoEmpty);
        RentBookInfo rentBookInfo = new RentBookInfo(appUsername, "2022-12-14");
        Book bookToRent = foundBook.withId(bookId).withAvailability(NOT_AVAILABLE).withRentBookInfo(rentBookInfo);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(foundBook));
        try {
            bookService.rentBook(bookId, rentBookInfo);
            fail();
        } catch (BookNotAvailableException e) {
            //then
            assertEquals("Book is not available", e.getMessage());
            verify(appUserRepository, never()).existsByUsername(appUsername);
            verify(bookRepository, never()).save(bookToRent);
        }
    }


    @Test
    void rentAlreadyRentedBook_returnBookIsAlreadyRentedException() {
        //given
        String bookId = "id-1";
        String username_1 = "user-1";
        String username_2 = "user-2";
        RentBookInfo rentBookInfo1 = new RentBookInfo(username_1, "2022-12-14");
        RentBookInfo rentBookInfo2 = new RentBookInfo(username_2, "2022-12-14");
        Book alreadyRentedBook = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                List.of(isbn_13), category, printType, pageCount,
                AVAILABLE,
                rentBookInfo1);

        Book bookToRent = alreadyRentedBook.withId(bookId).withAvailability(NOT_AVAILABLE).withRentBookInfo(rentBookInfo2);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(alreadyRentedBook));
        try {
            bookService.rentBook(bookId, rentBookInfo2);
            fail();
        } catch (BookIsAlreadyRentedException e) {
            //then
            assertEquals("Book is already rented to user: " + alreadyRentedBook.rentBookInfo().rentByUsername(), e.getMessage());
            verify(appUserRepository, never()).existsByUsername(username_2);
            verify(bookRepository, never()).save(bookToRent);
        }
    }

    @Test
    void rentBookByNotExistingUsernameInDb_returnUsernameNotFoundException() {
        //given
        String bookId = "id-1";
        String username = "user";
        RentBookInfo rentBookInfo = new RentBookInfo(username, "2022-12-12");
        Book book = new Book(bookId, null, "Java-Script", "P. Ackermann", List.of(isbn_13), category, printType, pageCount, AVAILABLE, rentBookInfoEmpty);
        Book bookToRent = book.withId(bookId).withAvailability(AVAILABLE).withRentBookInfo(rentBookInfo);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(appUserRepository.existsByUsername(username)).thenReturn(false);
        try {
            bookService.rentBook(bookId, rentBookInfo);
            fail();
        } catch (UserNotExistsByUsernameException e) {
            //then
            assertEquals("Username not found", e.getMessage());
            verify(bookRepository, never()).save(bookToRent);
        }
    }

    @Test
    void returnRentedBookById_returnUpdatedBook() {
        //given
        String bookId = "id-1";
        RentBookInfo rentBookInfo = new RentBookInfo("username", "2022-12-12");
        Book bookToReturn = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                List.of(isbn_13), category, printType, pageCount,
                NOT_AVAILABLE,
                rentBookInfo);

        Book bookAfterReturn = bookToReturn.withId(bookId)
                .withAvailability(AVAILABLE)
                .withRentBookInfo(rentBookInfoEmpty);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookToReturn));
        when(bookRepository.save(bookAfterReturn)).thenReturn(bookAfterReturn);

        Book actual = bookService.returnBook(bookId);

        //then
        verify(bookRepository).save(bookAfterReturn);
        assertEquals(bookAfterReturn, actual);
    }

    @Test
    void returnBookWithNotExistingBookId_returnNoSuchElementException() {
        //given
        String bookId = "id-1";
        RentBookInfo rentBookInfo = new RentBookInfo("username", "2022-12-12");
        Book bookToReturn = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                isbns, category, printType, pageCount,
                NOT_AVAILABLE,
                rentBookInfo);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        //when
        try {
            bookService.returnBook(bookId);
            fail();
        } catch (NoSuchElementException e) {
            //then
            assertEquals("No Book found with this ID", e.getMessage());
            verify(bookRepository, never()).save(bookToReturn);
        }
    }

    @Test
    void getMyRentedBooks_returnListOfBooks() {
        //given
        String username = "username";
        RentBookInfo rentBookInfo = new RentBookInfo("username", "2022-12-12");
        Book book = new Book("id1", null, "Java", "M. Kofler", isbns, category, printType, pageCount, NOT_AVAILABLE, rentBookInfo);
        List<Book> bookList = new ArrayList<>(List.of(book));

        //when
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> actual = bookService.getRentedBooks(username);

        //then
        verify(bookRepository).findAll();
        assertEquals(bookList, actual);
    }

    @Test
    void getMyRentedBooks_returnEmptyList() {
        //given
        String username = "username";
        List<Book> bookList = new ArrayList<>(List.of());

        //when
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> actual = bookService.getRentedBooks(username);

        //then
        verify(bookRepository).findAll();
        assertEquals(bookList, actual);
    }

    @Test
    void getMyRentedBooks_when_emptyRentBookInfo__returnEmptyList() {
        //given
        String username = "username";
        Book book = new Book("id1", null, "Java", "M. Kofler", isbns, category, printType, pageCount, NOT_AVAILABLE, null);
        List<Book> bookList = new ArrayList<>(List.of(book));
        List<Book> emptyBookList = new ArrayList<>(List.of());

        //when
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> myRentedBooks = bookService.getRentedBooks(username);

        //then
        verify(bookRepository).findAll();
        assertEquals(emptyBookList, myRentedBooks);
    }

    @Test
    void getMyRentedBooks_when_usernameFromRentBookInfoMismatch_returnEmptyList() {
        //given
        String username1 = "username1";
        String username2 = "username2";
        RentBookInfo rentBookInfo = new RentBookInfo(username1, "2022-12-12");
        Book book = new Book("id1", null, "Java", "M. Kofler", isbns, category, printType, pageCount, NOT_AVAILABLE, rentBookInfo);

        List<Book> bookList = new ArrayList<>(List.of(book));

        List<Book> emptyBookList = new ArrayList<>(List.of());

        //when
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> myRentedBooks = bookService.getRentedBooks(username2);

        //then
        verify(bookRepository).findAll();
        assertEquals(emptyBookList, myRentedBooks);
    }

}
