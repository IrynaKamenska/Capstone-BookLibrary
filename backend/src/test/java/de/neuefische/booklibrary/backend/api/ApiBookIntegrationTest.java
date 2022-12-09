package de.neuefische.booklibrary.backend.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.neuefische.booklibrary.backend.book.Availability;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class ApiBookIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private static MockWebServer mockWebServer;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @DynamicPropertySource
    static void backendProperties(DynamicPropertyRegistry registry) {
        registry.add("api.url", () -> mockWebServer.url("/").toString());

    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }


    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void searchApiBooksByIsbn_returnListWithOneBook() throws Exception {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLink thumbnail = new ImageLink("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_10, isbn_13), thumbnail, previewLink);
        ApiBook book = new ApiBook("5eDWcLzdAcYC", volumeInfo, Availability.AVAILABLE);

        BookResponseElement mockBokListResponse = new BookResponseElement(1, List.of(book));
        System.out.println("Response:" + mockBokListResponse);
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );


        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/isbn/3897214482"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                    {
                                        "id": "5eDWcLzdAcYC",
                                        "title": "Java von Kopf bis Fuß",
                                        "author": "Kathy Sierra",
                                        "isbn": "3897214482",
                                        "availability": "AVAILABLE"
                                    }
                                ]
                                """
                ));

    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void searchApiBooksByInvalidIsbn_return0TotalItems() throws Exception {
        String mockBookListResponse = """
                 {
                     "items": [],
                     "totalItems": 0
                 }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockBookListResponse)
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/isbn/abc"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                []
                                """
                ));
    }


    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void searchApiBooksByKeyword_returnListOfBooks() throws Exception {
        //given
        String previewLink = "http://books.google.de/books/preview";
        Isbn isbn_13 = new Isbn("ISBN_13", "9783897214484");
        Isbn isbn_10 = new Isbn("ISBN_10", "3897214482");
        ImageLink thumbnail = new ImageLink("http://books.google.com/books/thumbnail");
        VolumeInfo volumeInfo = new VolumeInfo("Java von Kopf bis Fuß", List.of("Kathy Sierra", "Bert Bates"), List.of(isbn_13, isbn_10), thumbnail, previewLink);
        ApiBook book = new ApiBook("5eDWcLzdAcYC", volumeInfo, Availability.AVAILABLE);

        BookResponseElement mockBokListResponse = new BookResponseElement(1, List.of(book));
        System.out.println("Response:" + mockBokListResponse);
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(mockBokListResponse))
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/search/java"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                 {"id":"5eDWcLzdAcYC",
                                 "title":"Java von Kopf bis Fuß",
                                 "author":"Kathy Sierra",
                                 "isbn":"9783897214484",
                                 "availability": "AVAILABLE"}]
                                 """
                ));

    }


    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void searchApiBooksByKeyword_returnZeroTotalItems() throws Exception {
        //given
        String mockBookListResponse = """
                 {
                     "items": [],
                     "totalItems": 0
                 }
                """;
        mockWebServer.enqueue(new MockResponse()
                .setBody(mockBookListResponse)
                .addHeader("Content-Type", "application/json")
        );

        //when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/search/12345dfjlkdfhdsjhfjkdsgfjkds"))
                // then
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

    }
}
