package com.example.store.order.api.model;

import java.util.List;

public record CreateOrderRequest(String customerId, String customerType, List<OrderLineRequest> lines) {
}
