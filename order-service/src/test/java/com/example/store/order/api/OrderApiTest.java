package com.example.store.order.api;

import com.example.store.order.support.CatalogStub;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class OrderApiTest {

    private static final String CREATE_ORDER = "/orders/create";

    @Autowired
    private MockMvc mvc;

    private CatalogStub catalog;

    @BeforeEach
    void startCatalog() throws Exception {
        catalog = new CatalogStub();
    }

    @AfterEach
    void stopCatalog() {
        catalog.close();
    }

    private static String order(String lines) {
        return "{\"customerId\":\"c-1\",\"customerType\":\"REGULAR\",\"lines\":" + lines + "}";
    }

    @Test
    @DisplayName("Task 3: a line without quantity is a client error")
    void missingQuantityIsABadRequest() throws Exception {
        mvc.perform(post(CREATE_ORDER).contentType(MediaType.APPLICATION_JSON)
                        .content(order("[{\"sku\":\"BOOK-1\"}]")))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Task 3: an unknown SKU is reported to the client, not as a server error")
    void unknownSkuIsNotFound() throws Exception {
        mvc.perform(post(CREATE_ORDER).contentType(MediaType.APPLICATION_JSON)
                        .content(order("[{\"sku\":\"NOPE\",\"quantity\":1}]")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Task 4: creating an order follows REST conventions")
    void createReturns201WithLocation() throws Exception {
        mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
                        .header("Idempotency-Key", "key-1")
                        .content(order("[{\"sku\":\"BOOK-1\",\"quantity\":1}]")))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Task 4: retrying the same checkout does not create a second order")
    void sameIdempotencyKeyReturnsTheSameOrder() throws Exception {
        String body = order("[{\"sku\":\"BOOK-1\",\"quantity\":1}]");
        MvcResult first = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
                        .header("Idempotency-Key", "key-2").content(body))
                .andExpect(status().is2xxSuccessful()).andReturn();
        MvcResult second = mvc.perform(post("/orders").contentType(MediaType.APPLICATION_JSON)
                        .header("Idempotency-Key", "key-2").content(body))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").exists()).andReturn();

        String firstId = com.jayway.jsonpath.JsonPath.read(first.getResponse().getContentAsString(), "$.id").toString();
        String secondId = com.jayway.jsonpath.JsonPath.read(second.getResponse().getContentAsString(), "$.id").toString();
        assertTrue(firstId.equals(secondId), "expected the same order, got " + firstId + " and " + secondId);
    }

    @Test
    @DisplayName("Task 5: an order with N lines does not call catalog-service N times")
    void catalogIsCalledOnceForAllLines() throws Exception {
        mvc.perform(post(CREATE_ORDER).contentType(MediaType.APPLICATION_JSON)
                .content(order("[{\"sku\":\"BOOK-1\",\"quantity\":1},{\"sku\":\"BOOK-2\",\"quantity\":1},"
                        + "{\"sku\":\"MUG-1\",\"quantity\":2}]")));

        assertTrue(catalog.requestCount() <= 1,
                "catalog-service was called " + catalog.requestCount() + " times for 3 lines");
    }
}
