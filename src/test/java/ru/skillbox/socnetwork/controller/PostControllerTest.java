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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@AutoConfigureEmbeddedDatabase
        (provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.OPENTABLE,
                refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
@Sql(value = {"/sql/001-create-schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/002-fill-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/003-delete-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthorizedAccessDeniedTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/post/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    @WithUserDetails("ilin@mail.ru")
    void searchPostByTextTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/post?text='title'"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("ilin@mail.ru")
    void searchPostByTextWithTagsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/post?text=title&tags=java,code"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getExistentPostByIdTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/post/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Path.of("src/test/resources/json/post_platform_likes/post.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getExistentPostByIdWithSubCommentsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/post/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Path.of("src/test/resources/json/post_platform_likes/post.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getExistentPostByIdWithoutCommentsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/post/2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Path
                                .of("src/test/resources/json/post_platform_likes/post_without_comments.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getNonexistentPostByIdTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/post/-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json("{\"error\":\"invalid_request\"," +
                        "\"error_description\":\"Incorrect post data, can't find this id -1\"}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getWallTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1/wall"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Path
                                .of("src/test/resources/json/post_platform_likes/wall.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getFeedsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/feeds"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Path
                                .of("src/test/resources/json/post_platform_likes/feeds.json"))));
    }

    @Test
    @WithUserDetails("petrov@mail.ru")
    void addPostTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/3/wall")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"new post\"," +
                                "\"post_text\":\"new post text\"," +
                                "\"tags\":[\"java\",\"new post\"]}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json((Files.readString(Path
                                .of("src/test/resources/json/post_platform_likes/new_post.json")))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void editPostWithTagTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"new edited post\"," +
                                "\"post_text\":\"new edited post text\"," +
                                "\"tags\":[\"java\",\"new post\",\"normal tag\"]}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Path
                                .of("src/test/resources/json/post_platform_likes/edited_post.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void editPostWithLongTagTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"new edited post\"," +
                                "\"post_text\":\"new edited post text\"," +
                                "\"tags\":[\"java\",\"new post\",\"very long phrase in english\"]}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error\":\"invalid_request\",\"error_description\":" +
                                "\"15 symbol's is MAX tag length, current length is 27\"}"));
    }

    @Test
    @WithUserDetails("petrov@mail.ru")
    void editPostNoPersonTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/post/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"new edited post\"," +
                                "\"post_text\":\"new edited post text\"," +
                                "\"tags\":[\"java\",\"new post\"]}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error\":\"invalid_request\",\"error_description\":" +
                                "\"You cannot edit this post, you are not the author. Author ID is 1\"}"));
    }

    @Test
    @WithUserDetails("petrov@mail.ru")
    void deleteNoPersonPostTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/post/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error\":\"invalid_request\",\"error_description\":" +
                                "\"You cannot delete this post, you are not the author. Author ID is 1\"}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deletePostTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/post/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"id\":1}}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteNonexistentPostTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/post/-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error_description\":\"Incorrect post data, can't find this id -1\"}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getCommentsPostTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/post/1/comments"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Path
                                .of("src/test/resources/json/post_platform_likes/comments_array.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void addCommentToPostTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/post/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment_text\": \"new comment\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Path
                                .of("src/test/resources/json/post_platform_likes/added_comment.json"))));
    }

    @Test
    @WithUserDetails("ilin@mail.ru")
    void addCommentToAnotherPersonPostTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/post/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment_text\": \"another new comment\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void editNotPersonCommentToPostTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/post/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment_text\": \"new comment\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error_description\":\"You cannot edit this comment, " +
                                "you are not the author. Author ID is 5\"}"));
    }

    @Test
    @WithUserDetails("ilin@mail.ru")
    void editCommentToPostTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/v1/post/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"comment_text\": \"new comment\"}"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Path
                                .of("src/test/resources/json/post_platform_likes/edited_comment.json"))));
    }

    @Test
    @WithUserDetails("petrov@mail.ru")
    void deleteCommentTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/post/1/comments/6"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"id\":6}}"));
    }

    @Test
    @WithUserDetails("ilin@mail.ru")
    void deleteCommentWithSubCommentsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/post/1/comments/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"id\":1}}"));
    }

    @Test
    @WithUserDetails("ilin@mail.ru")
    void deleteSubCommentsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/post/1/comments/11"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"data\":{\"id\":11}}"));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void deleteNotPersonCommentTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/post/1/comments/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error_description\":\"You cannot delete this comment, " +
                                "you are not the author. Author ID is 5\"}"));
    }

    @Test
    @WithUserDetails("ilin@mail.ru")
    void deleteNonexistentCommentTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/post/1/comments/-1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(SecurityMockMvcResultMatchers.authenticated())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error_description\":\"Incorrect comment data, can't find this id -1\"}"));
    }
}