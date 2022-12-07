package de.neuefische.booklibrary.backend.security;

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

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
public class AppUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void expect200_GET_login() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/app-users/login")
                        .header("Authorization", "Basic:" + Base64.getEncoder().encodeToString("user:user123".getBytes())))
                .andExpect(status().is(200));
    }



    @Test
    @WithMockUser
    void expect200_GET_me() throws Exception {
        mockMvc.perform(get("/api/app-users/me"))
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(roles = {"LIBRARIAN"})
    void expect200_GET_role_asLibrarian() throws Exception {
        mockMvc.perform(get("/api/app-users/role"))
                .andExpect(status().isOk())
                .andExpect(content().string("[ROLE_LIBRARIAN]"));
    }

    @Test
    @WithMockUser(roles = {"MEMBER"})
    void expect200_GET_role_asMember() throws Exception {
        mockMvc.perform(get("/api/app-users/role"))
                .andExpect(status().isOk())
                .andExpect(content().string("[ROLE_MEMBER]"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = {"LIBRARIAN"})
    void expect201_POST_addLibrarian() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "ira",
                                    "rawPassword": "Password898#",
                                    "role": "LIBRARIAN"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/app-users/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = {"MEMBER"})
    void expect201_POST_addMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "lars",
                                    "rawPassword": "Password899#"
                                }
                                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/app-users/login"))
                .andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = {"LIBRARIAN"})
    void expect409_conflict_POST_addLibrarian_WithAlreadyExistingName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "ira",
                                    "rawPassword": "Password898#"
                                }
                                """))
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "ira",
                                    "rawPassword": "Password898#"
                                }
                                """))
                .andExpect(status().isConflict());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = {"MEMBER"})
    void expect409_conflict_POST_addMember_WithAlreadyExistingName() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "lars",
                                    "rawPassword": "Password899#"
                                }
                                """))
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "lars",
                                    "rawPassword": "Password899#"
                                }
                                """))
                .andExpect(status().isConflict());
    }


    @Test
    @DirtiesContext
    @WithMockUser(username = "lars", roles = {"MEMBER"})
    void expect204_DELETE_deleteMember() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "lars",
                                    "rawPassword": "Password899#"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        AppUser appUser = objectMapper.readValue(body, AppUser.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/app-users/" + appUser.id()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = {"LIBRARIAN"})
    void expectAppUser_GET_AppUser() throws Exception {
        String body = mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "id1",
                                    "username": "user",
                                    "rawPassword": "Password30#",
                                    "role": "LIBRARIAN"
                                }
                                """))
                .andReturn().getResponse().getContentAsString();
        AppUser appUser = objectMapper.readValue(body, AppUser.class);

        mockMvc.perform(get("/api/app-users/user"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                    "id": "id1",
                                    "username": "user",
                                    "passwordBcrypt": "<encoded>",
                                    "role": "LIBRARIAN"
                                }
                                """.replace("<encoded>", appUser.passwordBcrypt())));
    }

}
