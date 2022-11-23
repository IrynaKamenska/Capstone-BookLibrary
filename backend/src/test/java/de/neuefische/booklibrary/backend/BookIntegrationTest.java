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
                                "isbn": "1-2-3",
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
                             "isbn": "1-2-3",
                             "bookState": "AVAILABLE"
                        } ]
                        """.replace("<id>", book.id())));

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
