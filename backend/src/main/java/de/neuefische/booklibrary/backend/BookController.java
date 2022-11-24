package de.neuefische.booklibrary.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Book addBook(@RequestBody @Valid Book newBook) {
        return bookService.saveBook(newBook);
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
