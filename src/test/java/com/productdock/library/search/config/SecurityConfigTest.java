package com.productdock.library.search.config;

import com.productdock.library.search.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class SecurityConfigTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenUnauthenticated_thenUnauthorizedResponse() throws Exception {
        mockMvc.perform(get("/api/search")
                        .param("page", "2"))
                .andExpect(status().isUnauthorized());
    }

}
