package de.neuefische.booklibrary.backend.api;


import de.neuefische.booklibrary.backend.book.Availability;
import de.neuefische.booklibrary.backend.book.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class ApiBookService {
    private final String QUERY = "?q=";
    private final String KEY = "&key=";
    public static final String DEFAULT_COVER = "https://cdn-icons-png.flaticon.com/512/117/117029.png";
    private final String apiKey;
    private final WebClient webClient;

    public ApiBookService(@Value("${api.url}") String apiUrl, @Value("${api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.create(apiUrl);
    }

    public List<Book> getApiBookByIsbn(String isbn) throws ResponseStatusException {
        String searchByIsbn = "isbn:";
        String query = QUERY + searchByIsbn + isbn + KEY + apiKey;
        ResponseEntity<BookResponseElement> bookResponse = getBookResponse(query);
        List<ApiBook> books = getBookList(bookResponse).stream().filter(book -> book.volumeInfo()
                .industryIdentifiers()
                .stream()
                .anyMatch(isbn1 -> isbn.equals(isbn1.identifier()))).toList();
        List<Book> bookList = new ArrayList<>();
        getBookListFromApiBookList(books, bookList);
        return bookList;

    }

    private static void getBookListFromApiBookList(List<ApiBook> books, List<Book> bookList) {
        for (ApiBook apiBook : books) {
            VolumeInfo volumeInfo = apiBook.volumeInfo();
            String thumbnail = Optional.ofNullable(volumeInfo.imageLink())
                    .map(ImageLink::thumbnail)
                    .orElse(DEFAULT_COVER);
            String author = Optional.ofNullable(volumeInfo.authors()).map(current -> current.get(0))
                    .orElse(null);
            Book book = new Book(
                    apiBook.id(),
                    thumbnail,
                    apiBook.volumeInfo().title(),
                    author,
                    apiBook.volumeInfo().industryIdentifiers().get(0).identifier(),
                    Availability.AVAILABLE, null);
            bookList.add(book);
        }
    }

    public List<Book> getAllApiBooks(String searchText) {
        String maxResults = "&maxResults=40";
        String query = QUERY + searchText + KEY + apiKey + maxResults;
        ResponseEntity<BookResponseElement> bookResponse = getBookResponse(query);
        List<ApiBook> books = getBookList(bookResponse);

        List<Book> bookList = new ArrayList<>();
        getBookListFromApiBookList(books, bookList);

        return bookList;
    }

    private ResponseEntity<BookResponseElement> getBookResponse(String query) {
        return requireNonNull(webClient
                .get()
                .uri(query)
                .retrieve()
                .toEntity(BookResponseElement.class)
                .block(), "ResponseEntity is null");

    }

    private static List<ApiBook> getBookList(ResponseEntity<BookResponseElement> bookResponse) {
        return Optional.ofNullable(bookResponse)
                .map(HttpEntity::getBody)
                .filter(body -> body.totalItems() >= 1)
                .map(BookResponseElement::apiBookItems)
                .stream()
                .flatMap(Collection::stream).toList();
    }
}
