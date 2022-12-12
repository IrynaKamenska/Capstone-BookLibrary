package de.neuefische.booklibrary.backend.book;

import de.neuefische.booklibrary.backend.security.AppUser;
import de.neuefische.booklibrary.backend.security.AppUserRepository;
import de.neuefische.booklibrary.backend.security.AppUserRole;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
        Book book = new Book("id1", null, "Java", "M. Kofler", "978-3-8362-8392-2", AVAILABLE, null);
        Book foundBook = book.withTitle("Java").withAuthor("M. Kofler").withIsbn("978-3-8362-8392-2").withAvailability(AVAILABLE);

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
        Book book = new Book(null, null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", NOT_AVAILABLE, null);
        Book saveBook = book.withTitle("Java-Script").withAuthor("P. Ackermann").withIsbn("978-3-8362-8629-9").withAvailability(NOT_AVAILABLE);
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
        Book book = new Book("id1", null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", NOT_AVAILABLE, null);
        Book saveBook = book.withId("id1").withTitle("Java-Script").withAuthor("P. Ackermann").withIsbn("978-3-8362-8629-9").withAvailability(NOT_AVAILABLE);
        when(bookRepository.save(saveBook)).thenReturn(saveBook);

        //when
        Book actual = bookService.saveBook(saveBook);

        //then
        assertEquals(saveBook, actual);
    }


    @Test
    void updateBookById_returnUpdatedBook() {
        //given
        Book book = new Book("id1", null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", NOT_AVAILABLE, null);
        Book toUpdateBook = book.withId("id1").withTitle("Java-Script").withAuthor("P. Ackermann").withIsbn("978-3-8362-8629-9").withAvailability(NOT_AVAILABLE);

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
        //given
        Book book = new Book("id1", null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", NOT_AVAILABLE, null);
        doNothing().when(bookRepository).deleteById(book.id());
        //when
        bookService.deleteBook(book.id());
        //then
        verify(bookRepository).deleteById(book.id());
    }

    @Test
    void rentExistingBookByIdAndUsername_returnUpdatedBook() {
        //given
        String bookId = "id-1";
        String appUsername = "username";
        Book foundBook = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                "978-3-8362-8629-9",
                AVAILABLE,
                null);

        AppUser appUser = new AppUser(
                "id-1",
                appUsername,
                "password",
                "",
                AppUserRole.MEMBER);


        Book bookToRent = foundBook.withId(bookId)
                .withAvailability(NOT_AVAILABLE)
                .withRentedBy(appUser.username());

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(foundBook));
        when(appUserRepository.existsByUsername(appUser.username())).thenReturn(true);
        when(bookRepository.save(bookToRent)).thenReturn(bookToRent);

        Book actual = bookService.rentBook(bookId, appUser.username());

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
                "978-3-8362-8629-9",
                AVAILABLE,
                null);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        //when
        try {
            bookService.rentBook(bookId, username);
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
                "978-3-8362-8629-9",
                NOT_AVAILABLE,
                null);

        Book bookToRent = foundBook.withId(bookId).withAvailability(NOT_AVAILABLE).withRentedBy(appUsername);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(foundBook));
        try {
            bookService.rentBook(bookId, appUsername);
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
        Book foundBook = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                "978-3-8362-8629-9",
                AVAILABLE,
                "user-1");

        Book bookToRent = foundBook.withId(bookId).withAvailability(AVAILABLE).withRentedBy(username_1);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(foundBook));
        try {
            bookService.rentBook(bookId, username_2);
            fail();
        } catch (BookIsAlreadyRentedException e) {
            //then
            assertEquals("Book is already rented to user: " + bookToRent.rentedBy(), e.getMessage());
            verify(appUserRepository, never()).existsByUsername(username_2);
            verify(bookRepository, never()).save(bookToRent);
        }
    }

    @Test
    void rentBookByNotExistingUsernameInDb_returnUsernameNotFoundException() {
        //given
        String bookId = "id-1";
        String username = "user";

        Book foundBook = new Book(bookId, null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", AVAILABLE, null);
        Book bookToRent = foundBook.withId(bookId).withAvailability(AVAILABLE).withRentedBy(username);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(foundBook));
        when(appUserRepository.existsByUsername(username)).thenReturn(false);
        try {
            bookService.rentBook(bookId, username);
            fail();
        } catch (UsernameNotFoundException e) {
            //then
            assertEquals("Username not found", e.getMessage());
            verify(bookRepository, never()).save(bookToRent);
        }
    }

    @Test
    void returnBookById_returnUpdatedBook() {
        //given
        String bookId = "id-1";
        Book book = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                "978-3-8362-8629-9",
                AVAILABLE,
                null);

        Book bookToReturn = book.withId(bookId)
                .withAvailability(AVAILABLE)
                .withRentedBy(null);

        //when
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);

        Book actual = bookService.returnBook(bookId);

        //then
        verify(bookRepository).save(book);
        assertEquals(bookToReturn, actual);
    }

    @Test
    void returnBookWithNotExistingBookId_returnNoSuchElementException() {
        //given
        String bookId = "id-1";
        Book book = new Book(
                bookId,
                null,
                "Java-Script",
                "P. Ackermann",
                "978-3-8362-8629-9",
                AVAILABLE,
                "Bob");

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());
        //when
        try {
            bookService.returnBook(bookId);
            fail();
        } catch (NoSuchElementException e) {
            //then
            assertEquals("No Book found with this ID", e.getMessage());
            verify(bookRepository, never()).save(book);
        }
    }

    @Test
    void getRentedByMeBooks_returnListOfBooks() {
        //given
        String username = "username";
        Book book = new Book("id1", null, "Java", "M. Kofler", "978-3-8362-8392-2", NOT_AVAILABLE, username);
        List<Book> bookList = new ArrayList<>(List.of(book));

        //when
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> actual = bookService.getRentedBooks(username);

        //then
        verify(bookRepository).findAll();
        assertEquals(bookList, actual);
    }

    @Test
    void getRentedByMeBooks_returnEmptyList() {
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

}
