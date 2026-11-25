package com.example.store.order.domain;

import java.math.BigDecimal;

public final class OrderItem {

    private final Long id;
    private final String sku;
    private final String name;
    private final BigDecimal unitPrice;
    private final int quantity;

    public OrderItem(String sku, String name, BigDecimal unitPrice, int quantity) {
        this(null, sku, name, unitPrice, quantity);
    }

    private OrderItem(Long id, String sku, String name, BigDecimal unitPrice, int quantity) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku is required");
        }
        if (quantity < 1) {
            throw new IllegalArgumentException("quantity must be at least 1");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (unitPrice == null || unitPrice.signum() < 0) {
            throw new IllegalArgumentException("unitPrice must be non-negative");
        }
        this.id = id;
        this.sku = sku;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public static OrderItem restore(Long id, String sku, String name, BigDecimal unitPrice, int quantity) {
        return new OrderItem(id, sku, name, unitPrice, quantity);
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal lineTotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
