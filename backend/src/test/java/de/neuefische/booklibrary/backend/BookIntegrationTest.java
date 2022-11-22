package de.neuefische.booklibrary.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
class BookIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;


    @Test
    void getAllBooks_ifNoBooksExist_returnEmptyList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    @DirtiesContext
    void addBookWithoutId_returnBook() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "bookState": "AVAILABLE"
                                }
                                """))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {
                            "id": "<id>",
                            "title": "Java",
                             "author": "Ullenbom",
                             "isbn": "ISBN 978-0-596-52068-7",
                             "bookState": "AVAILABLE"
                        } ]
                        """.replace("<id>", book.id())));

    }

    @Test
    @DirtiesContext
    void updateBook_return200() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "bookState": "AVAILABLE"
                                }
                                """))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/"+book.id())
                .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": "<id>",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "bookState": "NOT_AVAILABLE"
                                }
                            """.replace("<id>",book.id())))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                            "id": "<id>",
                            "title": "Java",
                            "author": "Ullenbom",
                            "isbn": "ISBN 978-0-596-52068-7",
                            "bookState": "NOT_AVAILABLE"
                        }
                    """.replace("<id>",book.id())));
    }

    @Test
    @DirtiesContext
    void updateBook_ifUrlIdMismatchRequestBodyId_return400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/id1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": "id2",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "bookState": "NOT_AVAILABLE"
                                }
                            """))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("The ID in the URL does not match the request body's ID"));




    }

    @Test
    @DirtiesContext
    void deleteBook_return204() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "1-2-3",
                                "bookState": "AVAILABLE"
                                }
                                """))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/"+book.id()))
                .andExpect(status().isNoContent());

    }


}
