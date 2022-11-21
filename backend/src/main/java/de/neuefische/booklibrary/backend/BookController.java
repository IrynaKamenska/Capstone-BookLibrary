package de.neuefische.booklibrary.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    List<Book> getAllBooks (){
        return bookService.getAllBooks();
    }

    @PostMapping
    @ResponseStatus(code= HttpStatus.CREATED)
    public Book addBook(@RequestBody Book newBook) {
        return bookService.saveBook(newBook);
    }
}
