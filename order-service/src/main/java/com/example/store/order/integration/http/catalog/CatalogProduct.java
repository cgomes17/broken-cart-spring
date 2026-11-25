package com.example.store.order.integration.http.catalog;

import java.math.BigDecimal;

public record CatalogProduct(String sku, String name, BigDecimal price) {

    public CatalogProduct {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("price must be non-negative");
        }
    }
}
