package com.example.store.order.domain.exception;

public class ProductNotFoundException extends Exception {

    public ProductNotFoundException(String sku) {
        super("Unknown product: " + sku);
    }
}
