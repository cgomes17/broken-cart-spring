package com.example.store.web.api.controller;

import com.example.store.web.integration.http.status.ServiceStatusClient;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StatusController.class)
class StatusControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ServiceStatusClient statusClient;

    @Test
    void showsEachServiceAsUp() throws Exception {
        when(statusClient.check()).thenReturn(Map.of("catalog-service", true));

        mvc.perform(get("/status"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("catalog-service")))
                .andExpect(content().string(containsString("All services are up")));
    }

    @Test
    void showsDownServices() throws Exception {
        Map<String, Boolean> result = new LinkedHashMap<>();
        result.put("catalog-service", true);
        result.put("order-service", false);
        when(statusClient.check()).thenReturn(result);

        mvc.perform(get("/status"))
                .andExpect(content().string(containsString("DOWN")))
                .andExpect(content().string(containsString("Some services are down")));
    }
}
