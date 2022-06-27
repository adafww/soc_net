package ru.skillbox.socnetwork.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@AutoConfigureEmbeddedDatabase
        (provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.OPENTABLE,
                refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
@Sql(value = {"/sql/001-create-schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/002-fill-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/003-delete-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("test@mail.ru")
    void blockUserFromInitiatorTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/block/2"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/positive_response.json"))));
    }

    @Test
    @WithUserDetails("ilin@mail.ru")
    void blockUserFromFocusPersonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/block/1"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/positive_response.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void blockUserAgainFromFocusPersonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/block/7"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description")
                        .value("Blocking is not possible, because user in relation to the current " +
                                "user is already blocked."));
    }

    @Test
    @WithUserDetails("onufriy1@mail.ru")
    void blockUserAgainFromInitiatorTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/block/1"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description")
                        .value("Blocking is not possible, because user in relation to the current " +
                                "user is already blocked."));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void blockUserOnNonExistentRelationship() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/block/10"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/positive_response.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void blockYourselfTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/block/1"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description").value("You can't block yourself."));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void blockNonExistentUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/users/block/112312"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description")
                        .value("Request denied. Failed to get user with specified id in database."));
    }

    @Test
    void unauthorizedAccessAddFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/block/4"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
