package de.neuefische.booklibrary.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.booklibrary.backend.book.Availability;
import de.neuefische.booklibrary.backend.book.Book;
import de.neuefische.booklibrary.backend.book.RentBookInfo;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ApiBookServiceTest {
    private static MockWebServer mockWebServer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ApiBookService apiBookService;

    private final RentBookInfo rentBookInfoEmpty = new RentBookInfo("", "");
    private final Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
    private final Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
    private final List<Isbn> isbns = new ArrayList<>(List.of(isbn_13, isbn_10));
    private final String previewLink = "http://books.google.de/books/preview";
    private final ImageLink thumbnail = new ImageLink("http://books.google.com/books/thumbnail");
    private final String category = "Fiction";
    private final String printType = "BOOK";
    private final int pageCount = 100;

    private final String headUrl = "http://localhost:%s";
   private final VolumeInfo volumeInfo = new VolumeInfo(
           "Java von Kopf bis Fuß",
           List.of("Kathy Sierra", "Bert Bates"),
           isbns, thumbnail, previewLink,
           List.of(category),
           "BOOK",500);
    @Value("${api.key}")
    private String apiKey;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @BeforeEach
    void initialize() {
        String baseUrl = String.format(headUrl, mockWebServer.getPort());
        apiBookService = new ApiBookService(baseUrl, apiKey);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }


    @Test
    void getApiBookByIsbn_returnOneBook() throws Exception {
        //given
        ApiBook book = new ApiBook("5eDWcLzdAcYC", volumeInfo, Availability.AVAILABLE);
        ApiBook foundBook = book.withVolumeInfo(volumeInfo.withTitle("Java von Kopf bis Fuß").withAuthors(List.of("Kathy Sierra", "Bert Bates")).withIndustryIdentifiers(List.of(isbn_10))
                .withImageLink(thumbnail).withPreviewLink(previewLink).withPageCount(100));

        BookResponseElement mockBokListResponse = new BookResponseElement(1, List.of(foundBook));
        List<Book> expected = List.of(new Book(
                "5eDWcLzdAcYC", "http://books.google.com/books/thumbnail", "Java von Kopf bis Fuß",
                "Kathy Sierra", List.of(isbn_10), category, printType, pageCount, Availability.AVAILABLE, rentBookInfoEmpty));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );
        // when
        List<Book> actual = apiBookService.getApiBookByIsbn("3897214482");

        String isbnQueryUrl = "?q=isbn:3897214482&key=null";
        HttpUrl expectedUrl = mockWebServer.url(String.format(headUrl, mockWebServer.getPort()) + isbnQueryUrl);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals(expectedUrl, recordedRequest.getRequestUrl());

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
        ApiBook book = new ApiBook("5eDWcLzdAcYC", volumeInfo, Availability.AVAILABLE);
        ApiBook foundBook = book.withVolumeInfo(volumeInfo.withTitle("Java von Kopf bis Fuß").withAuthors(List.of("Kathy Sierra", "Bert Bates")).withIndustryIdentifiers(isbns)
                .withImageLink(thumbnail).withPreviewLink(previewLink).withPageCount(100).withCategories(List.of(category)).withPrintType("BOOK"));

        BookResponseElement mockBokListResponse = new BookResponseElement(1,
                List.of(foundBook));

        List<Book> expected = List.of(new Book(
                "5eDWcLzdAcYC", "http://books.google.com/books/thumbnail", "Java von Kopf bis Fuß",
                "Kathy Sierra", isbns, category, printType, pageCount, Availability.AVAILABLE, rentBookInfoEmpty));

        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );
        // when
        List<Book> actual = apiBookService.getAllApiBooks("Java");

        String keyWordQueryUrl = "/?q=Java&key=null&maxResults=10";
        HttpUrl expectedUrl = mockWebServer.url(String.format(headUrl, mockWebServer.getPort()) + keyWordQueryUrl);
        RecordedRequest recordedRequest = mockWebServer.takeRequest();

        //then
        assertEquals(expected, actual);
        assertEquals("GET", recordedRequest.getMethod());
        assertEquals(expectedUrl, recordedRequest.getRequestUrl());

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
