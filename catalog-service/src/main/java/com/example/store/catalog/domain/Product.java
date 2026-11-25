package com.example.store.catalog.domain;

import java.math.BigDecimal;
import java.util.Map;

public record Product(String sku, String name, String category, BigDecimal price, Map<String, String> attributes) {

    public Product {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("sku is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (category == null || category.isBlank()) {
            throw new IllegalArgumentException("category is required");
        }
        if (price == null || price.signum() < 0) {
            throw new IllegalArgumentException("price must be non-negative");
        }
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }
}
