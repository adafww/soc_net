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

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@AutoConfigureEmbeddedDatabase
        (provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.OPENTABLE,
                refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
@Sql(value = {"/sql/001-create-schema.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/002-fill-tables.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/sql/003-delete-tables.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class DialogsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("test@mail.ru")
    void createDialogTest() throws Exception {

        List<Integer> id = List.of(11);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/dialogs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(Paths.get("src/test/resources/json/dialog/create_dialog_rq.json")))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("string"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(11));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getDialogsTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/dialogs"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Paths.get("src/test/resources/json/dialog/dialog_rs.json"))));

    }

    @Test
    @WithUserDetails("test@mail.ru")
    void sendMessageTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/dialogs/1/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(Files.readAllBytes(Paths.get("src/test/resources/json/dialog/send_message_rq.json")))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Paths.get("src/test/resources/json/dialog/send_message_rs.json"))));
    }

    @Test
    @WithUserDetails("test@mail.ru")
    void getMessagesByDialogIdTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/dialogs/1/messages"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(Files.readString(Paths.get("src/test/resources/json/dialog/messages_by_dialog_id_rs.json"))));
    }
}
