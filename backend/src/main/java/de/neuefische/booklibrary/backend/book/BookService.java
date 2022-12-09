package de.neuefische.booklibrary.backend.book;

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
        return bookRepository.save(book);
    }

    public Book updateBook(Book updatedBook) {
        return bookRepository.save(updatedBook);
    }

    public void deleteBook(String id) {
        bookRepository.deleteById(id);
    }

    public boolean isBookExisting(String id) {
        return bookRepository.existsById(id);
    }
}
