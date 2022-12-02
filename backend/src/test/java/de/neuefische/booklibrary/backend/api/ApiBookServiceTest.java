package de.neuefische.booklibrary.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.booklibrary.backend.Book;
import de.neuefische.booklibrary.backend.BookRepository;
import de.neuefische.booklibrary.backend.BookState;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;


public class ApiBookServiceTest {
    private static MockWebServer mockWebServer;
    private final BookRepository bookRepository = mock(BookRepository.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ApiBookService apiBookService;

    @Value("${api.key}")
    private String apiKey;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format("http://localhost:%s",
                mockWebServer.getPort());
        apiBookService = new ApiBookService(bookRepository, baseUrl, apiKey);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }


    @Test
    void getApiBookByIsbn_returnOneBook() throws Exception {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);
        ApiBook book = new ApiBook("5eDWcLzdAcYC", volumeInfo, BookState.AVAILABLE);
        ApiBook foundBook = book.withVolumeInfo(volumeInfo.withTitle("Java von Kopf bis Fuß").withAuthors(List.of("Kathy Sierra", "Bert Bates")).withIndustryIdentifiers(List.of(isbn_13, isbn_10))
                .withImageLinks(thumbnail).withPreviewLink(previewLink));


        BookResponseElement mockBokListResponse = new BookResponseElement(1,
                List.of(foundBook));

        List<Book> expected = List.of(new Book("5eDWcLzdAcYC", "Java von Kopf bis Fuß", "Kathy Sierra", "9783897214484", BookState.AVAILABLE));


        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );
        // when
        List<Book> actual = apiBookService.getApiBookByIsbn("9783897214484");
        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());

    }


    @Test
    void getApiBookByInvalidIsbn_returnZeroBooks() throws Exception {
        //given
        BookResponseElement mockBokListResponse = new BookResponseElement(0, List.of());
        List<Book> expected = List.of();

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );

        // when
        List<Book> actual = apiBookService.getApiBookByIsbn("abc");
        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());

    }


    @Test
    void getApiBookByKeyWord_returnOneBook() throws Exception {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLinks thumbnail = new ImageLinks("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);
        ApiBook book = new ApiBook("5eDWcLzdAcYC", volumeInfo, BookState.AVAILABLE);

        ApiBook foundBook = book.withVolumeInfo(volumeInfo.withTitle("Java von Kopf bis Fuß").withAuthors(List.of("Kathy Sierra", "Bert Bates")).withIndustryIdentifiers(List.of(isbn_13, isbn_10))
                .withImageLinks(thumbnail).withPreviewLink(previewLink));

        BookResponseElement mockBokListResponse = new BookResponseElement(1,
                List.of(foundBook));

        List<Book> expected = List.of(new Book("5eDWcLzdAcYC", "Java von Kopf bis Fuß", "Kathy Sierra", "9783897214484", BookState.AVAILABLE));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );
        // when
        List<Book> actual = apiBookService.getAllApiBooks("Java");
        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());

    }


    @Test
    void getApiBookByNotExistingKeyword_returnZeroBooks() throws Exception {
        //given
        BookResponseElement mockBokListResponse = new BookResponseElement(0, List.of());
        List<Book> expected = List.of();
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );

        // when
        List<Book> actual = apiBookService.getAllApiBooks("12345dfjlkdfhdsjhfjkdsgfjkds");
        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());

    }

}