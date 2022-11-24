package de.neuefische.booklibrary.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public Book updateBook(@RequestBody @Valid Book updatedBook, @PathVariable String id) {
        if (updatedBook.id().equals(id)) {
            return bookService.updateBook(updatedBook);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The ID in the URL does not match the request body's ID");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
    }
}
