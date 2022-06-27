package ru.skillbox.socnetwork.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
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
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCaptchaTest() throws Exception {

        this.mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/account/register"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{}"));
    }

    @Test
    void correctRegistrationTest() throws Exception {
        for (int i = 0; i < 60; i++) {
            this.mockMvc.perform(MockMvcRequestBuilders
                    .get("/api/v1/account/register"));
        }
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/account/register")
                .content("{\"captcha_id\": 1655752447821," +
                        "\"code\": \"psvm\"," +
                        "\"email\": \"t@mail.ru\"," +
                        "\"firstName\": \"Test\"," +
                        "\"lastName\": \"Short\"," +
                        "\"passwd1\": \"11111111\"," +
                        "\"passwd2\": \"11111111\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void incorrectPasswordRegistrationTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/account/register")
                        .content("{\"captcha_id\": 1655752447821," +
                                "\"code\": \"psvm\"," +
                                "\"email\": \"t@mail.ru\"," +
                                "\"firstName\": \"Test\"," +
                                "\"lastName\": \"Short\"," +
                                "\"passwd1\": \"11111112\"," +
                                "\"passwd2\": \"11111111\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void incorrectEmailRegistrationTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/account/register")
                        .content("{\"captcha_id\": 1655752447821," +
                                "\"code\": \"psvm\"," +
                                "\"email\": \"tmail.ru\"," +
                                "\"firstName\": \"Test\"," +
                                "\"lastName\": \"Short\"," +
                                "\"passwd1\": \"11111111\"," +
                                "\"passwd2\": \"11111111\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error_description\":\"Incorrect email, please try again\"}"));
    }

    @Test
    void incorrectCaptchaRegistrationTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/account/register")
                        .content("{\"captcha_id\": 1655752447821," +
                                "\"code\": \"1234\"," +
                                "\"email\": \"t@mail.ru\"," +
                                "\"firstName\": \"Test\"," +
                                "\"lastName\": \"Short\"," +
                                "\"passwd1\": \"11111111\"," +
                                "\"passwd2\": \"11111111\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error_description\":\"Incorrect captcha, please try again\"}"));
    }

    @Test
    void incorrectCaptchaIsEmptyRegistrationTest() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/account/register")
                        .content("{\"email\": \"t@mail.ru\"," +
                                "\"firstName\": \"Test\"," +
                                "\"lastName\": \"Short\"," +
                                "\"passwd1\": \"11111111\"," +
                                "\"passwd2\": \"11111111\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error_description\":\"Incorrect captcha, please try again (is null)\"}"));
    }

    @Test
    void correctRegistrationWithCaptchaTest() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/v1/account/register")
                        .content("{\"email\": \"t@mail.ru\"," +
                                "\"firstName\": \"Test\"," +
                                "\"lastName\": \"Short\"," +
                                "\"passwd1\": \"11111111\"," +
                                "\"passwd2\": \"11111111\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content()
                        .json("{\"error_description\":\"Incorrect captcha, please try again (is null)\"}"));
    }
}