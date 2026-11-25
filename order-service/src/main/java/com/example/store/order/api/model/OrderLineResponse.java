package com.example.store.order.api.model;

import com.example.store.order.domain.OrderItem;

import java.math.BigDecimal;

public record OrderLineResponse(String sku, String name, BigDecimal unitPrice, int quantity) {

    public static OrderLineResponse from(OrderItem item) {
        return new OrderLineResponse(item.getSku(), item.getName(), item.getUnitPrice(), item.getQuantity());
    }
}
