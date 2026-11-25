package com.example.store.order.api.model;

import com.example.store.order.domain.Order;

import java.math.BigDecimal;
import java.util.List;

public record OrderResponse(Long id,
                            String customerId,
                            String customerType,
                            String status,
                            BigDecimal total,
                            List<OrderLineResponse> lines) {

    public OrderResponse {
        lines = List.copyOf(lines);
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(order.getId(), order.getCustomerId(), order.getCustomerType(), order.getStatus(),
                order.getTotal(), order.getItems().stream().map(OrderLineResponse::from).toList());
    }
}
