package com.example.store.web.domain;

import java.math.BigDecimal;

public record CartItem(String sku, String name, BigDecimal unitPrice, int quantity) {

    public CartItem {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unitPrice must be non-negative");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be at least 1");
        }
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
