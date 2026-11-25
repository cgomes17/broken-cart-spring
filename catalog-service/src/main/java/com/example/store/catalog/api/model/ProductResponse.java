package com.example.store.catalog.api.model;

import com.example.store.catalog.domain.Product;

import java.math.BigDecimal;
import java.util.Map;

public record ProductResponse(String sku, String name, String category, BigDecimal price,
                              Map<String, String> attributes) {

    public ProductResponse {
        attributes = Map.copyOf(attributes);
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.sku(), product.name(), product.category(), product.price(),
                product.attributes());
    }
}
