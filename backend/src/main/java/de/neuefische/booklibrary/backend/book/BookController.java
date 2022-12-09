package de.neuefische.booklibrary.backend.book;

import de.neuefische.booklibrary.backend.api.ApiBookService;
import de.neuefische.booklibrary.backend.security.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    private final AppUserService appUserService;
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


    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Book addBook(@RequestBody @Valid Book newBook) {
        if (newBook.cover() == null) {
            return bookService.saveBook(newBook.withCover(DEFAULT_COVER));
        }
        return bookService.saveBook(newBook);
    }


    @PostMapping("/rentBook/{id}/{rentedBy}")
    public Book rentBook(@PathVariable String id, @PathVariable String rentedBy, @RequestBody Book book) {
        if (appUserService.existsByUsername(rentedBy)) {
            return bookService.rentBook(id, rentedBy, book);
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No User with name:" + rentedBy + " found");
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
