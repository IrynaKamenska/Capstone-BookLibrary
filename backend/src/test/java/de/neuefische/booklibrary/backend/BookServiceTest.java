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
        List<Book> expected = books;
        assertEquals(expected, actual);

    }


   @Test
   void getAllBooks_ifBooksExist_returnListOfAllBooks(){
        //given
       List<Book> bookList = new ArrayList<>(List.of(
               new Book("id1", "Java", "M. Kofler", "978-3-8362-8392-2", BookState.AVAILABLE)
       ));
       when(bookRepository.findAll()).thenReturn(bookList);

       //when
       List<Book> actual = bookService.getAllBooks();

       //then
       List<Book> expected = bookList;
       assertEquals(expected, actual);

   }
}
