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
        assertEquals(books, actual);

    }


    @Test
    void getAllBooks_ifBooksExist_returnListOfAllBooks() {
        //given
        List<Book> bookList = new ArrayList<>(List.of(
                new Book("id1", "Java", "M. Kofler", "978-3-8362-8392-2", BookState.AVAILABLE)
        ));
        when(bookRepository.findAll()).thenReturn(bookList);

        //when
        List<Book> actual = bookService.getAllBooks();

        //then
        assertEquals(bookList, actual);

    }


    @Test
    void addNewBookWithoutId_returnBookWithId() {
        //given
        Book book = new Book(null, "Java-Script", "P. Ackermann", "978-3-8362-8629-9", BookState.NOT_AVAILABLE);
        when(bookRepository.insert(book)).thenReturn(book.withId("id1"));

        //when
        Book actual = bookService.saveBook(book);
        Book expected = book.withId("id1");

        //then
        verify(bookRepository).insert(book);
        assertEquals(expected, actual);
    }

    @Test
    void addNewBookWithId_returnBook() {
        //given
        Book book = new Book("id1", "Java-Script", "P. Ackermann", "978-3-8362-8629-9", BookState.NOT_AVAILABLE);
        when(bookRepository.insert(book)).thenReturn(book);

        //when
        Book actual = bookService.saveBook(book);
        Book expected = book;

        //then
        verify(bookRepository).insert(book);
        assertEquals(book, actual);
    }

    @Test
    void deleteBookById(){
        //given
        String id = "id1";
        doNothing().when(bookRepository).deleteById(id);
        //when
        bookService.deleteBook(id);
        //then
        verify(bookRepository).deleteById(id);
    }


   @Test
    void updateBookById_returnUpdatedBook(){
        //given
       Book book = new Book("id1", "Java-Script", "P. Ackermann", "978-3-8362-8629-9", BookState.NOT_AVAILABLE);
       when(bookRepository.save(book)).thenReturn(book);

       //when
       Book actual = bookService.updateBook(book);
       Book expected = book;

       //then
       verify(bookRepository).save(book);
       assertEquals(expected, actual);
   }


}
