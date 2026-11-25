package com.example.store.catalog.api.controller;

import com.example.store.catalog.domain.Product;
import com.example.store.catalog.repository.ProductRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    private static final Product BOOK =
            new Product("BOOK-1", "Clean Code", "books", new BigDecimal("39.90"), Map.of("author", "Robert C. Martin"));

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProductRepository products;

    @Test
    void returnsProductWithCacheControl() throws Exception {
        when(products.findBySku("BOOK-1")).thenReturn(Optional.of(BOOK));

        mvc.perform(get("/products/BOOK-1"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "max-age=300"))
                .andExpect(jsonPath("$.sku").value("BOOK-1"))
                .andExpect(jsonPath("$.name").value("Clean Code"))
                .andExpect(jsonPath("$.category").value("books"))
                .andExpect(jsonPath("$.price").value(39.90))
                .andExpect(jsonPath("$.attributes.author").value("Robert C. Martin"));
    }

    @Test
    void unknownSkuReturns404() throws Exception {
        when(products.findBySku("NOPE")).thenReturn(Optional.empty());

        mvc.perform(get("/products/NOPE")).andExpect(status().isNotFound());
    }

    @Test
    void batchLookupBySku() throws Exception {
        when(products.findAllBySku(List.of("BOOK-1", "MUG-1"))).thenReturn(List.of(BOOK));

        mvc.perform(get("/products").param("sku", "BOOK-1", "MUG-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].sku").value("BOOK-1"));
    }

    @Test
    void filtersByCategory() throws Exception {
        when(products.findByCategory("books")).thenReturn(List.of(BOOK));

        mvc.perform(get("/products").param("category", "books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value("books"));
    }

    @Test
    void returnsAllProducts() throws Exception {
        when(products.findAll()).thenReturn(List.of(BOOK));

        mvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("BOOK-1"));
    }
}
