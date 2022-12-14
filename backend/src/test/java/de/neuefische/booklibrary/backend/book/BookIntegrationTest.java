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
                                "isbn": [
                                         {
                                             "type": "ISBN_10",
                                             "identifier": "3527713646"
                                         },
                                         {
                                             "type": "ISBN_13",
                                             "identifier": "9783527713646"
                                         }
                                     ],
                                "availability": "AVAILABLE",
                                    "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
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
                                 "isbn": [
                                     {
                                         "type": "ISBN_10",
                                         "identifier": "3527713646"
                                     },
                                     {
                                         "type": "ISBN_13",
                                         "identifier": "9783527713646"
                                     }
                                 ],
                             "availability": "AVAILABLE",
                              "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                 }
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
                                   "isbn": [
                                         {
                                             "type": "ISBN_10",
                                             "identifier": "3527713646"
                                         }
                                     ],
                                "availability": "AVAILABLE",
                                 "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
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
                           "isbn": [
                                 {
                                     "type": "ISBN_10",
                                     "identifier": "3527713646"
                                 }
                             ],
                             "availability": "AVAILABLE",
                             "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
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
                                 "isbn": [
                                         {
                                             "type": "ISBN_10",
                                             "identifier": "3527713646"
                                         }
                                     ],
                                "availability": "AVAILABLE",
                                  "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
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
                                   "isbn": [
                                         {
                                             "type": "ISBN_10",
                                             "identifier": "3527713646"
                                         }
                                     ],
                                    "availability": "NOT_AVAILABLE",
                                     "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
                                    }
                                """.replace("<id>", book.id())).with(csrf()))
                .andExpect(status().is(200))
                .andExpect(content().json("""
                            {
                                "id": "<id>",
                                "title": "Java",
                                "author": "Ullenbom",
                               "isbn": [
                                     {
                                         "type": "ISBN_10",
                                         "identifier": "3527713646"
                                     }
                                 ],
                                "availability": "NOT_AVAILABLE",
                                 "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
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
                                "isbn": [
                                         {
                                             "type": "ISBN_13",
                                             "identifier": "9783527713646"
                                         }
                                     ],
                                "availability": "AVAILABLE",
                                 "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
                                }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andExpect(content().json("""
                        {
                                "id": "1",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": [
                                         {
                                             "type": "ISBN_13",
                                             "identifier": "9783527713646"
                                         }
                                     ],
                                "availability": "AVAILABLE",
                                 "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
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
                                    "isbn": [
                                         {
                                             "type": "ISBN_13",
                                             "identifier": "9783527713646"
                                         }
                                     ],
                                    "availability": "NOT_AVAILABLE",
                                    "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
                                    }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andExpect(content().json("""
                        {
                                "id": "id1",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": [
                                         {
                                             "type": "ISBN_13",
                                             "identifier": "9783527713646"
                                         }
                                     ],
                                "availability": "NOT_AVAILABLE",
                                  "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
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
                                 "isbn": [
                                     {
                                         "type": "ISBN_13",
                                         "identifier": "9783527713646"
                                     }
                                     ],
                                "availability": "AVAILABLE",
                                 "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
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
    @WithMockUser(roles = {"LIBRARIAN"}, username = "member")
    @DirtiesContext
    void rentBookByIdOnExistingAppUser_return200() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": "id-1",
                                "cover": "http://localhost:8080/api/cover",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": [
                                         {
                                             "type": "ISBN_13",
                                             "identifier": "9783527713646"
                                         }
                                     ],
                                "availability": "AVAILABLE",
                                 "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
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


        mockMvc.perform(MockMvcRequestBuilders.post("/api/books/rentBook/" + book.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "rentByUsername": "member",
                                    "rentUntil": "2022-12-19"
                                }
                                """).with(csrf()))
                .andExpect(status().is(200))
                .andExpect(content().json("""
                        {
                                "id": "id-1",
                                "cover": "http://localhost:8080/api/cover",
                                "title": "Java",
                                "author": "Ullenbom",
                                "isbn": [
                                         {
                                             "type": "ISBN_13",
                                             "identifier": "9783527713646"
                                         }
                                     ],
                                "availability": "NOT_AVAILABLE",
                                 "rentBookInfo": {
                                    "rentByUsername": "member",
                                    "rentUntil": "2022-12-19"
                                     }
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
                                 "isbn": [
                                         {
                                             "type": "ISBN_13",
                                             "identifier": "9783527713646"
                                         }
                                     ],
                                "availability": "NOT_AVAILABLE",
                                 "rentBookInfo": {
                                    "rentByUsername": "member",
                                    "rentUntil": "2022-12-19"
                                     }
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
                                 "isbn": [
                                         {
                                             "type": "ISBN_13",
                                             "identifier": "9783527713646"
                                         }
                                     ],
                                "availability": "AVAILABLE",
                                "rentBookInfo": {
                                    "rentByUsername": "",
                                    "rentUntil": ""
                                     }
                        }
                        """));
    }


    @Test
    @WithMockUser(roles = {"LIBRARIAN"}, username = "member")
    @DirtiesContext
    void getRentedBooks_returnListWithOneBook() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "id": "id-1",
                                 "cover": "http://books.google.com",
                                 "title": "Ich und meine Schwester Klara",
                                 "author": "Dimiter Inkiow",
                                 "isbn": [
                                         {
                                             "type": "ISBN_10",
                                             "identifier": "9783527713"
                                         }
                                     ],
                                 "availability": "AVAILABLE",
                                 "rentBookInfo": {
                                     "rentByUsername": "",
                                     "rentUntil": ""
                                         }
                                   }
                                """).with(csrf()))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        Book book = objectMapper.readValue(body, Book.class);


        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "member",
                                    "rawPassword": "Password898#",
                                    "role": "MEMBER"
                                }
                                """).with(csrf()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/app-users/login"))
                .andExpect(status().isOk());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/books/rentBook/" + book.id())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "rentByUsername": "member",
                                    "rentUntil": "2022-12-14"
                                }
                                """).with(csrf()))
                .andExpect(status().is(200));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/books/rentedBooks"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                        {
                            "id": "id-1",
                             "cover": "http://books.google.com",
                             "title": "Ich und meine Schwester Klara",
                             "author": "Dimiter Inkiow",
                             "isbn": [
                                         {
                                             "type": "ISBN_10",
                                             "identifier": "9783527713"
                                         }
                                     ],
                             "availability": "NOT_AVAILABLE",
                             "rentBookInfo": {
                                 "rentByUsername": "member",
                                 "rentUntil": "2022-12-14"
                             }
                                 }
                        ]
                                        """));
    }


}
