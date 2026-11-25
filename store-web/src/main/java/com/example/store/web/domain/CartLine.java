package com.example.store.web.domain;

import java.math.BigDecimal;

final class CartLine {

    private final String sku;
    private final String name;
    private final BigDecimal unitPrice;
    private int quantity;

    CartLine(String sku, String name, BigDecimal unitPrice) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unitPrice must be non-negative");
        }
        this.sku = sku;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    String sku() {
        return sku;
    }

    String name() {
        return name;
    }

    BigDecimal unitPrice() {
        return unitPrice;
    }

    int quantity() {
        return quantity;
    }

    void increase() {
        quantity++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartLine other)) {
            return false;
        }
        return sku == other.sku;
    }

    @Override
    public int hashCode() {
        return sku.hashCode();
    }
}
