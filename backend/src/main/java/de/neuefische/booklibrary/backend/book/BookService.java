package de.neuefische.booklibrary.backend.book;

import de.neuefische.booklibrary.backend.security.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

import static de.neuefische.booklibrary.backend.book.Availability.AVAILABLE;
import static de.neuefische.booklibrary.backend.book.Availability.NOT_AVAILABLE;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AppUserRepository appUserRepository;


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

    public Book rentBook(String id, String rentedBy) {
        Book bookToRent = bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No Book found with this ID"));

        if (bookToRent.availability() != AVAILABLE) throw new BookNotAvailableException("Book is not available");
        if ((bookToRent.rentedBy() != null)) {
            throw new BookIsAlreadyRentedException("Book is already rented to user: " + bookToRent.rentedBy());
        }

        if (!appUserRepository.existsByUsername(rentedBy)) {
            throw new UserNotExistsByUsernameException("Username not found");
        }
        bookToRent = bookToRent.withId(id).withRentedBy(rentedBy).withAvailability(NOT_AVAILABLE);
        return bookRepository.save(bookToRent);

    }

    public Book returnBook(String id) {
        Book bookToReturn = bookRepository.findById(id).orElseThrow(() -> new NoSuchElementException("No Book found with this ID"));
        bookToReturn = bookToReturn.withId(id).withRentedBy(null).withAvailability(AVAILABLE);
        return bookRepository.save(bookToReturn);
    }

    public List<Book> getRentedBooks(String username) {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .filter(book -> book.rentedBy() != null && book.rentedBy().equals(username)).collect(toList());
    }
}
