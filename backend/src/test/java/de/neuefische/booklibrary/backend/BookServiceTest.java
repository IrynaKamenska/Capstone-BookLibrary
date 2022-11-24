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
        Book book = new Book("id1", "Java", "M. Kofler", "978-3-8362-8392-2", BookState.AVAILABLE);
        Book foundBook = book.withTitle("Java").withAuthor("M. Kofler").withIsbn("978-3-8362-8392-2").withBookState(BookState.AVAILABLE);

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
        Book book = new Book(null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", BookState.NOT_AVAILABLE);
        when(bookRepository.save(book)).thenReturn(book.withId("id1"));

        //when
        Book actual = bookService.saveBook(book);
        Book expected = book.withId("id1");

        //then
        assertEquals(expected, actual);
    }

    @Test
    void addNewBookWithId_returnBook() {
        //given
        Book book = new Book("id1", "Java-Script", "P. Ackermann", "978-3-8362-8629-9", BookState.NOT_AVAILABLE);
        when(bookRepository.save(book)).thenReturn(book);

        //when
        Book actual = bookService.saveBook(book);

        //then
        assertEquals(book, actual);
    }


   @Test
    void updateBookById_returnUpdatedBook(){
        //given
       Book book = new Book("id1", "Java-Script", "P. Ackermann", "978-3-8362-8629-9", BookState.NOT_AVAILABLE);
       when(bookRepository.save(book)).thenReturn(book);

       //when
       Book actual = bookService.updateBook(book);

       //then
       verify(bookRepository).save(book);
       assertEquals(book, actual);
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
        Book book = new Book("id1", "Java-Script", "P. Ackermann", "978-3-8362-8629-9", BookState.NOT_AVAILABLE);
        doNothing().when(bookRepository).deleteById(book.id());
        //when
        bookService.deleteBook(book.id());
        //then
        verify(bookRepository).deleteById(book.id());
    }

}
