package de.neuefische.booklibrary.backend.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
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
    @WithMockUser(roles = "LIBRARIAN")
    @DirtiesContext
    void addBookWithoutId_returnBook() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "cover": "http://localhost:8080/api/cover",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "AVAILABLE"
                                }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {
                            "id": "<id>",
                            "cover": "http://localhost:8080/api/cover",
                            "title": "Java",
                             "author": "Ullenbom",
                             "isbn": "ISBN 978-0-596-52068-7",
                             "availability": "AVAILABLE"
                        } ]
                        """.replace("<id>", book.id())))
                .andExpect(jsonPath("$..title").isNotEmpty())
                .andExpect(jsonPath("$..author").isNotEmpty())
                .andExpect(jsonPath("$..isbn").isNotEmpty())
                .andExpect(jsonPath("$..availability").isNotEmpty());

    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @DirtiesContext
    void addBookWithoutCover_returnBookWithCover() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "AVAILABLE"
                                }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {
                            "id": "<id>",
                            "cover": "<cover>",
                            "title": "Java",
                             "author": "Ullenbom",
                             "isbn": "ISBN 978-0-596-52068-7",
                             "availability": "AVAILABLE"
                        } ]
                        """.replace("<id>", book.id()).replace("<cover>", book.cover())))
                .andExpect(jsonPath("$..cover").isNotEmpty())
                .andExpect(jsonPath("$..title").isNotEmpty())
                .andExpect(jsonPath("$..author").isNotEmpty())
                .andExpect(jsonPath("$..isbn").isNotEmpty())
                .andExpect(jsonPath("$..availability").isNotEmpty());

    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @DirtiesContext
    void updateBookWithExistingId_return200() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "cover": "http://localhost:8080/api/cover",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "AVAILABLE"
                                }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/" + book.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "id": "<id>",
                                    "title": "Java",
                                    "author": "Ullenbom",
                                    "isbn": "ISBN 978-0-596-52068-7",
                                    "availability": "NOT_AVAILABLE"
                                    }
                                """.replace("<id>", book.id())).with(csrf()))
                .andExpect(status().is(200))
                .andExpect(content().json("""
                            {
                                "id": "<id>",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "NOT_AVAILABLE"
                            }
                        """.replace("<id>", book.id())));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @DirtiesContext
    void updateBookWithNewId_return201() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": "1",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "AVAILABLE"
                                }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andExpect(content().json("""
                        {
                                "id": "1",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "AVAILABLE"
                        }
                        """));
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @DirtiesContext
    void updateBook_withDifferentIdInUrlAndBody_return201() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/books/id1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                    {
                                    "id": "id2",
                                    "title": "Java",
                                    "author": "Ullenbom",
                                    "isbn": "ISBN 978-0-596-52068-7",
                                    "availability": "NOT_AVAILABLE"
                                    }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andExpect(content().json("""
                        {
                                "id": "id1",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "NOT_AVAILABLE"
                                }
                        """));


    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @DirtiesContext
    void deleteBookSuccesfull_return204() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "cover": "http://localhost:8080/api/cover",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "AVAILABLE"
                                }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/" + book.id()).with(csrf()))
                .andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    @DirtiesContext
    void deleteBookWithNotExistingId_return404() throws Exception {
        String id = "1";
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/books/" + id).with(csrf()))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("No Book with ID:" + id + " found"));
    }

    @Test
    @WithMockUser(roles = {"LIBRARIAN"}, username = "librarian")
    @DirtiesContext
    void rentBookByIdOnExistingInDbAppUser_return200() throws Exception {
        String rentOn = "member";
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": "id-1",
                                "cover": "http://localhost:8080/api/cover",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "AVAILABLE"
                                }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "librarian",
                                    "rawPassword": "Password898#",
                                    "role": "LIBRARIAN"
                                }
                                """).with(csrf()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/app-users/login"))
                .andExpect(status().isOk());


        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "member",
                                    "rawPassword": "Password899#"
                                }
                                """).with(csrf()))
                .andExpect(status().isCreated());


        mockMvc.perform(MockMvcRequestBuilders.post("/api/books/rentBook/" + book.id() + "/" + rentOn).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                                "id": "id-1",
                                "cover": "http://localhost:8080/api/cover",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "NOT_AVAILABLE",
                                "rentedBy": "member"
                        }
                        """));
    }


    @Test
    @WithMockUser(roles = {"LIBRARIAN"}, username = "librarian")
    @DirtiesContext
    void returnRentedBookById_return200() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": "id-1",
                                "cover": "http://localhost:8080/api/cover",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "NOT_AVAILABLE",
                                 "rentedBy": "member"
                                }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "librarian",
                                    "rawPassword": "Password898#",
                                    "role": "LIBRARIAN"
                                }
                                """).with(csrf()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/app-users/login"))
                .andExpect(status().isOk());


        mockMvc.perform(MockMvcRequestBuilders.post("/api/books/returnBook/" + book.id()).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {
                                "id": "id-1",
                                "cover": "http://localhost:8080/api/cover",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": "ISBN 978-0-596-52068-7",
                                "availability": "AVAILABLE",
                                "rentedBy": null
                        }
                        """));
    }

}
