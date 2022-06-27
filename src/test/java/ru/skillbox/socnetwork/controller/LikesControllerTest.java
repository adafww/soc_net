package ru.skillbox.socnetwork.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@AutoConfigureEmbeddedDatabase
        (provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.OPENTABLE,
                refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
@Sql(value = {"/sql/001-create-schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/002-fill-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/003-delete-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class LikesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("test@mail.ru")
    void getPostLike() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/likes?item_id=1&type=Post"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"likes\":2,\"users\":[1,2]}}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getUnknownLike() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/likes?item_id=1&type=Unknown"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error\":\"invalid_request\"," +
                                "\"error_description\":\"Bad like type. Required 'Post' or 'Comment' types\"}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getIsPostLiked() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/liked?item_id=1&type=Post"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"likes\":true}}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getIsUnknownLiked() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/liked?item_id=1&type=Unknown"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error\":\"invalid_request\"," +
                                "\"error_description\":\"Bad like type. Required 'Post' or 'Comment' types\"}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void putLikeToLikedPost() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/v1/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"item_id\":1,\"type\":\"Post\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"likes\":2,\"users\":[1,2]}}"));
    }

    @Test
    @WithUserDetails("petrov@mail.ru")
    void putLikeToPost() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/v1/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"item_id\":1,\"type\":\"Post\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"likes\":3,\"users\":[1,2,3]}}"));
    }

    @Test
    @WithUserDetails("petrov@mail.ru")
    void putLikeToUnknown() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/v1/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"item_id\":1,\"type\":\"Unknown\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error\":\"invalid_request\"," +
                                "\"error_description\":\"Bad like type. Required 'Post' or 'Comment' types\"}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteLikeFromLikedPost() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/likes?item_id=1&type=Post"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"likes\":1,\"users\":[2]}}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteLikeFromLikedComment() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/likes?item_id=1&type=Comment"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"likes\":0,\"users\":[]}}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteUnknownLike() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/likes?item_id=1&type=Unknown"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error\":\"invalid_request\"," +
                                "\"error_description\":\"Bad like type. Required 'Post' or 'Comment' types\"}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getCommentLike() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/likes?item_id=1&type=Comment"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error\":\"string\",\"data\":{\"likes\":0,\"users\":[]}}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getIsCommentLiked() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/liked?item_id=1&type=Comment"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"likes\":false}}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void putAndGetCommentLike() throws Exception {
        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .put("/api/v1/likes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"item_id\":1,\"type\":\"Comment\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error\":\"string\",\"data\":{\"likes\":1,\"users\":[1]}}"));
    }
}
