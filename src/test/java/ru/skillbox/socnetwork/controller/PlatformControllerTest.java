package ru.skillbox.socnetwork.controller;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
class PlatformControllerTest {

    private final static String CITIES_DATA =
            "{\"data\":[\"Perm\",\"Tbilisi\",\"Tver\",\"Saratov\",\"Alma-Ata\",\"Kaliningrad\"]}";

    private final static String COUNTRIES_DATA = "{\"data\":[\"Russia\",\"Georgia\",\"Kazakhstan\"]}";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getCountries() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/platform/countries"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(COUNTRIES_DATA));
    }

    @Test
    void getCities() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/platform/cities"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(CITIES_DATA));
    }

    @Test
    void getLanguages() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/platform/languages"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{\"data\":[\"Русский\"]}"));
    }
}
