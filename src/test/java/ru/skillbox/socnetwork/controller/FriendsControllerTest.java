package ru.skillbox.socnetwork.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
class FriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("test@mail.ru")
    void getFriendsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/friends"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(
                        Files.readString(Path.of("src/test/resources/json/friends_controller_test/friends.json"))));
    }

    @Test
    void unauthorizedAccessGetFriendsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/friends"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteFriendRequestTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/request/person/3"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/positive_response.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteFriendRequestFromYourselfTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/request/person/1"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description")
                        .value("Cannot apply this operation to itself."));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteFriendRequestNonExistentTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/request/person/27"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description")
                        .value("Deletion failed. The specified friend request was not found."));
    }

    @Test
    void unauthorizedAccessDeleteFriendRequestTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/request/person/3"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getListFriendRequestsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/friends/request"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/friend_requests.json"))));
    }

    @Test
    void unauthorizedAccessGetListFriendRequestsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/friends/request"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getListRecommendedFriendsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/friends/recommendations"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/recommended_friends.json"))));
    }

    @Test
    void unauthorizedAccessGetListRecommendedFriendsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/friends/recommendations"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getInformationAboutFriendshipsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/is/friends")
                        .contentType(MediaType.APPLICATION_JSON).content(Files.readString(Path.of("src/test" +
                                "/resources/json/friends_controller_test/friendships_with_specified_users.json"))))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(Path.of("src/test/resources" +
                        "/json/friends_controller_test/information_about_friendships.json"))));
    }

    @Test
    void unauthorizedAccessGetInformationAboutFriendshipsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/is/friends")
                        .contentType(MediaType.APPLICATION_JSON).content(Files.readString(Path.of("src/test" +
                                "/resources/json/friends_controller_test/friendships_with_specified_users.json"))))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void addFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/4"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/positive_response.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void acceptFriendRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/3"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/positive_response.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void addFriendYourselfTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/1"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description")
                        .value("You can't send a request to yourself."));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void addFriendAgainFromInitiatorTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/2"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description").value("It is not possible to apply " +
                        "as a friend, because these users are already friends."));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void addFriendAgainFromFocusPersonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/9"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description").value("It is not possible to apply " +
                        "as a friend, because these users are already friends."));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void sendFriendRequestAgainTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/8"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description").value("It is not possible to submit a " +
                        "request to add as a friend, as it has already been submitted earlier."));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void addFriendBlockedUserFromFocusPersonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/7"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description").value("The request is not possible " +
                        "because the specified user is blocked."));
    }

    @Test
    @WithUserDetails("onufriy1@mail.ru")
    void addFriendBlockedUserFromInitiatorTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/1"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description").value("The request is not possible " +
                        "because the specified user is blocked."));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    public void addFriendNonExistentUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/1232133"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description").value("Request denied. " +
                        "Failed to get user with specified id in database."));
    }

    @Test
    void unauthorizedAccessAddFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteFriendFromInitiatorTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/5"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/positive_response.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteFriendFromFocusPersonTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/9"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(Files.readString(
                        Path.of("src/test/resources/json/friends_controller_test/positive_response.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteNonExistentFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/112398"))
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(jsonPath("error").value("invalid_request"))
                .andExpect(jsonPath("error_description").value("Deletion is not possible. " +
                        "No friendly relationship found between the specified user."));
    }

    @Test
    void unauthorizedAccessDeleteFriendTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/friends/2"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}
