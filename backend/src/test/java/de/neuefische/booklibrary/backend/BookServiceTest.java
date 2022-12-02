package de.neuefische.booklibrary.backend;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookServiceTest {

    private final BookRepository bookRepository = mock(BookRepository.class);
    private final BookService bookService = new BookService(bookRepository);


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
        Book book = new Book("id1", null, "Java", "M. Kofler", "978-3-8362-8392-2", Availability.AVAILABLE);
        Book foundBook = book.withTitle("Java").withAuthor("M. Kofler").withIsbn("978-3-8362-8392-2").withAvailability(Availability.AVAILABLE);

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
        Book book = new Book(null, null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", Availability.NOT_AVAILABLE);
        Book saveBook = book.withTitle("Java-Script").withAuthor("P. Ackermann").withIsbn("978-3-8362-8629-9").withAvailability(Availability.NOT_AVAILABLE);
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
        Book book = new Book("id1", null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", Availability.NOT_AVAILABLE);
        Book saveBook = book.withId("id1").withTitle("Java-Script").withAuthor("P. Ackermann").withIsbn("978-3-8362-8629-9").withAvailability(Availability.NOT_AVAILABLE);
        when(bookRepository.save(saveBook)).thenReturn(saveBook);

        //when
        Book actual = bookService.saveBook(saveBook);

        //then
        assertEquals(saveBook, actual);
    }


   @Test
    void updateBookById_returnUpdatedBook() {
       //given
       Book book = new Book("id1", null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", Availability.NOT_AVAILABLE);
       Book toUpdateBook = book.withId("id1").withTitle("Java-Script").withAuthor("P. Ackermann").withIsbn("978-3-8362-8629-9").withAvailability(Availability.NOT_AVAILABLE);

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
    void deleteBookById() {
        //given
        Book book = new Book("id1", null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", Availability.NOT_AVAILABLE);
        doNothing().when(bookRepository).deleteById(book.id());
        //when
        bookService.deleteBook(book.id());
        //then
        verify(bookRepository).deleteById(book.id());
    }

}
