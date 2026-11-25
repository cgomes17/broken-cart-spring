package com.example.store.order.integration.http.catalog;

import org.springframework.web.client.RestClient;

public class CatalogClient {

    private final RestClient restClient;

    public CatalogClient(String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    public CatalogProduct findBySku(String sku) {
        return restClient.get().uri("/products/{sku}", sku).retrieve().body(CatalogProduct.class);
    }
}
