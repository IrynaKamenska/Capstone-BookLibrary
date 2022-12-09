package de.neuefische.booklibrary.backend.book;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

    public Book rentBook(String id, String rentedBy, Book book) {
        if (book.availability().equals(Availability.AVAILABLE)) {
            Book bookToRent = book.withId(id).withRentedBy(rentedBy).withAvailability(Availability.NOT_AVAILABLE);
            return bookRepository.save(bookToRent);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book with a status NOT_AVAILABLE cannot be rented");
        }
    }

}
