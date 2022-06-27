package ru.skillbox.socnetwork;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@AutoConfigureEmbeddedDatabase
		(provider = AutoConfigureEmbeddedDatabase.DatabaseProvider.OPENTABLE,
				refresh = AutoConfigureEmbeddedDatabase.RefreshMode.AFTER_CLASS)
class SocialNetworkApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void unauthorizedAccessDeniedTest() throws Exception {
		this.mockMvc.perform(get("/api/v1/post/1"))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}
}