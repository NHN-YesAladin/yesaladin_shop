package shop.yesaladin.shop.common;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthCheckController.class)
class HealthCheckControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void healthCheck() throws Exception {
        mockMvc.perform(get("/monitor/l7check"))
                .andExpect(status().isOk());
    }
}