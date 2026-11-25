package com.example.store.web.integration.http.order;

import java.util.List;

public record PlaceOrderRequest(String customerId, String customerType, List<Line> lines) {

    public PlaceOrderRequest {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId is required");
        }
        if (lines == null || lines.isEmpty()) {
            throw new IllegalArgumentException("at least one order line is required");
        }
        if (lines.stream().anyMatch(line -> line == null)) {
            throw new IllegalArgumentException("order lines must not contain null");
        }
        customerType = customerType == null ? "REGULAR" : customerType;
        lines = List.copyOf(lines);
    }

    public record Line(String sku, int quantity) {

        public Line {
            if (sku == null || sku.isBlank()) {
                throw new IllegalArgumentException("sku is required");
            }
            if (quantity < 1) {
                throw new IllegalArgumentException("quantity must be at least 1");
            }
        }
    }
}
