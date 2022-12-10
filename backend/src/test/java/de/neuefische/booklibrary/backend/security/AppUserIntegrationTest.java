package de.neuefische.booklibrary.backend.security;

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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class AppUserIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void expect200_GET_login() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/app-users/login")
                        .header("Authorization", "Basic:" + Base64.getEncoder().encodeToString("user:user123".getBytes())))
                .andExpect(status().is(200));
    }


    @Test
    @WithMockUser(username = "appUser")
    void expect200_GET_me() throws Exception {
        mockMvc.perform(get("/api/app-users/me"))
                .andExpect(status().isOk())
                .andExpect(content().string("appUser"));
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
    @WithMockUser(roles = {"LIBRARIAN"}, username = "appUser")
    @DirtiesContext
    void expect201_POST_addLibrarian() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "appUser",
                                    "rawPassword": "Password898#",
                                    "role": "LIBRARIAN"
                                }
                                """).with(csrf()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/app-users/login"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/app-users/me"))
                .andExpect(status().isOk())
                .andExpect(content().string("appUser"));
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = {"MEMBER"}, username = "appUser")
    void expect201_POST_addMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "appUser",
                                    "rawPassword": "Password899#"
                                }
                                """).with(csrf()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/app-users/login"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/app-users/role"))
                .andExpect(status().isOk())
                .andExpect(content().string("[ROLE_MEMBER]"));
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
                                """).with(csrf()))
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "ira",
                                    "rawPassword": "Password898#"
                                }
                                """).with(csrf()))
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
                                """).with(csrf()))
                .andExpect(status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "lars",
                                    "rawPassword": "Password899#"
                                }
                                """).with(csrf()))
                .andExpect(status().isConflict());
    }


    @Test
    @DirtiesContext
    @WithMockUser(username = "lars", roles = {"MEMBER"})
    void expect204_DELETE_deleteMember() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "lars",
                                    "rawPassword": "Password899#"
                                }
                                """).with(csrf()))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/app-users/deleteMe").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DirtiesContext
    @WithMockUser(roles = {"LIBRARIAN"})
    void expectAppUserInfo_GET_AppUserInfo() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "id1",
                                    "username": "user",
                                    "rawPassword": "Password30#",
                                    "role": "LIBRARIAN"
                                }
                                """).with(csrf()))
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/api/app-users/user"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                    "username": "user",
                                    "role": "LIBRARIAN"
                                }
                                """));
    }


    @Test
    @WithMockUser(roles = {"LIBRARIAN"}, username = "appUser")
    @DirtiesContext
    void expectOneUsername_GET_getAllUsernames() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/app-users/librarian")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "appUser",
                                    "rawPassword": "Password898#",
                                    "role": "LIBRARIAN"
                                }
                                """).with(csrf()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/app-users/login"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/app-users/me"))
                .andExpect(status().isOk())
                .andExpect(content().string("appUser"));

        mockMvc.perform(get("/api/app-users/getAllUsernames"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            "appUser"
                        ]
                        """));

    }

}
