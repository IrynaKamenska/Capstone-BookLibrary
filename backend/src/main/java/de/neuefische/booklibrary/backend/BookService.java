package de.neuefische.booklibrary.backend;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;


    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book saveBook(Book book) {
        return bookRepository.insert(book);
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }
}
