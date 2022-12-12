package de.neuefische.booklibrary.backend.book;

import de.neuefische.booklibrary.backend.api.ApiBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

import static de.neuefische.booklibrary.backend.api.ApiBookService.DEFAULT_COVER;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final ApiBookService apiBookService;
    private final BookService bookService;

    @GetMapping
    List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }


    @GetMapping("/search/{searchText}")
    public List<Book> getAllApiBooks(@PathVariable String searchText) {
        return apiBookService.getAllApiBooks(searchText);
    }

    @GetMapping("/isbn/{isbn}")
    public List<Book> getApiBookByIsbn(@PathVariable @Valid String isbn) {
        return apiBookService.getApiBookByIsbn(isbn);
    }

    @GetMapping("/rentedBooks")
    public List<Book> getRentedBooks() {
        String user = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        return bookService.getRentedBooks(user);
    }


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Book addBook(@RequestBody @Valid Book newBook) {
        if (newBook.cover() == null) {
            return bookService.saveBook(newBook.withCover(DEFAULT_COVER));
        }
        RentBookInfo rentBookInfo = new RentBookInfo("", "");
        return bookService.saveBook(newBook.withRentBookInfo(rentBookInfo));
    }


    @PostMapping("/rentBook/{id}")
    public Book rentBook(@PathVariable String id, @RequestBody RentBookInfo rentBookInfo) {
        return bookService.rentBook(id, rentBookInfo);
    }

    @PostMapping("/returnBook/{id}")
    public Book returnBook(@PathVariable String id) {
        return bookService.returnBook(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@RequestBody @Valid Book book, @PathVariable String id) {
        boolean bookExists = bookService.isBookExisting(id);

        Book bookToUpdate = book.withId(id);
        Book updatedBook = bookService.saveBook(bookToUpdate);

        return bookExists ?
                new ResponseEntity<>(updatedBook, HttpStatus.OK) :
                new ResponseEntity<>(updatedBook, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        if (bookService.isBookExisting(id)) {
            bookService.deleteBook(id);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Book with ID:" + id + " found");
    }
}
