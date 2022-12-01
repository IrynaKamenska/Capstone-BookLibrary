package de.neuefische.booklibrary.backend.api;


import de.neuefische.booklibrary.backend.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

@Service
public class ApiBookService {

    private final BookRepository bookRepository;

    private final String apiKey;
    private final WebClient webClient;

    public ApiBookService(BookRepository bookRepository, @Value("${api.url}") String apiUrl, @Value("${api.key}") String apiKey) {
        this.bookRepository = bookRepository;
        this.apiKey = apiKey;
        this.webClient = WebClient.create(apiUrl);
    }

    String maxResults = "&maxResults=5";


    public BookResponseElement getApiBookByIsbn(String isbn) throws ResponseStatusException {
        String query = "?q=isbn:" + isbn + "&key=" + apiKey;
        ResponseEntity<BookResponseElement> bookResponse = getBookResponse(query);
        List<ApiBook> books = getBookList(bookResponse).stream().filter(book -> book.volumeInfo()
                .industryIdentifiers()
                .stream()
                .anyMatch(isbn1 -> isbn.equals(isbn1.identifier()))).toList();
        int totalItems = requireNonNull(bookResponse.getBody()).totalItems();
        return new BookResponseElement(totalItems, books);

    }

    public BookResponseElement getAllApiBooks(String searchText) {
        String query = "?q=" + searchText + "&key=" + apiKey + maxResults;
        ResponseEntity<BookResponseElement> bookResponse = getBookResponse(query);
        List<ApiBook> books = getBookList(bookResponse);
        int totalItems = requireNonNull(bookResponse.getBody()).totalItems();
        return new BookResponseElement(totalItems, books);

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
        List<ApiBook> books = Optional.ofNullable(bookResponse)
                .map(HttpEntity::getBody)
                .filter(body -> body.totalItems() >= 1)
                .map(BookResponseElement::apiBookItems)
                .stream()
                .flatMap(Collection::stream).toList();
        return books;
    }


}
