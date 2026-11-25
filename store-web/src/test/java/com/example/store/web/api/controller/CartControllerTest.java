package com.example.store.web.api.controller;

import com.example.store.web.integration.http.catalog.Product;
import com.example.store.web.integration.http.catalog.CatalogClient;
import com.example.store.web.integration.http.order.OrderClient;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CatalogClient catalog;

    @MockitoBean
    private OrderClient orders;

    @Test
    @DisplayName("Task 7: one user's cart is not visible to another user")
    void cartIsNotSharedBetweenSessions() throws Exception {
        when(catalog.findBySku("BOOK-1")).thenReturn(
                new Product("BOOK-1", "Clean Code", "books", new BigDecimal("39.90"), Map.of()));
        MockHttpSession alice = new MockHttpSession();
        MockHttpSession bob = new MockHttpSession();

        mvc.perform(post("/cart/items").param("sku", "BOOK-1").session(alice))
                .andExpect(status().is3xxRedirection());

        mvc.perform(get("/cart").session(alice))
                .andExpect(content().string(containsString("Clean Code")));
        mvc.perform(get("/cart").session(bob))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Clean Code"))));
    }

    @Test
    void invalidCheckoutInputShowsAnErrorAndPreservesTheCart() throws Exception {
        when(catalog.findBySku("BOOK-1")).thenReturn(
                new Product("BOOK-1", "Clean Code", "books", new BigDecimal("39.90"), Map.of()));
        MockHttpSession session = new MockHttpSession();
        mvc.perform(post("/cart/items").param("sku", "BOOK-1").session(session))
                .andExpect(status().is3xxRedirection());

        mvc.perform(post("/cart/checkout").session(session)
                        .param("customerId", " ").param("customerType", "REGULAR")
                        .param("idempotencyKey", "invalid-customer"))
                .andExpect(redirectedUrl("/cart"))
                .andExpect(flash().attribute("error", "customerId is required"));

        verifyNoInteractions(orders);
        mvc.perform(get("/cart").session(session))
                .andExpect(content().string(containsString("Clean Code")));
    }
}
