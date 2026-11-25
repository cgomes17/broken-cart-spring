package com.example.store.order.service;

import com.example.store.order.api.model.CreateOrderRequest;
import com.example.store.order.api.model.OrderLineRequest;
import com.example.store.order.domain.Order;
import com.example.store.order.integration.http.catalog.CatalogClient;
import com.example.store.order.integration.http.catalog.CatalogProduct;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService service;

    @MockitoBean
    private CatalogClient catalog;

    @Test
    @DisplayName("Task 6: OrderService can be tested without catalog-service running")
    void createUsesTheCatalogItWasGiven() throws Exception {
        when(catalog.findBySku("BOOK-1")).thenReturn(new CatalogProduct("BOOK-1", "Clean Code", new BigDecimal("39.90")));

        Order order = service.create(new CreateOrderRequest("c-1", "REGULAR",
                List.of(new OrderLineRequest("BOOK-1", 2))));

        assertEquals(new BigDecimal("79.80"), order.getTotal());
    }
}
