package com.example.store.order.repository;

import com.example.store.order.domain.Order;
import com.example.store.order.domain.OrderItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class OrderPersistenceTest {

    @Autowired
    private OrderRepository orders;

    @Autowired
    private MockMvc mvc;

    private Order savedOrder() {
        Order order = new Order("c-1", "REGULAR", null);
        order.addItem(new OrderItem("BOOK-1", "Clean Code", new BigDecimal("39.90"), 2));
        return orders.save(order);
    }

    @Test
    @DisplayName("Task 9: a saved order can be loaded again with its lines")
    void savedOrderCanBeReadBack() {
        Long id = savedOrder().getId();

        Order loaded = orders.findById(id).orElseThrow();

        assertEquals(1, loaded.getItems().size());
        assertEquals(2, loaded.getItems().getFirst().getQuantity());
    }

    @Test
    @DisplayName("Task 9: the order confirmation endpoint returns the order")
    void confirmationEndpointReturnsTheOrder() throws Exception {
        Long id = savedOrder().getId();

        mvc.perform(get("/orders/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lines[0].sku").value("BOOK-1"));
    }
}
