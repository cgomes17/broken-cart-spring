package com.example.store.web.integration.http.catalog;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CatalogClient {

    private static final ParameterizedTypeReference<List<Product>> PRODUCT_LIST =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    public CatalogClient(RestClient.Builder builder, @Value("${store.catalog-url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public List<Product> findAll(String category) {
        Optional<String> filter = Optional.ofNullable(category).filter(c -> !c.isBlank());
        return restClient.get()
                .uri(uri -> uri.path("/products").queryParamIfPresent("category", filter).build())
                .retrieve()
                .body(PRODUCT_LIST);
    }

    public Product findBySku(String sku) {
        return restClient.get().uri("/products/{sku}", sku).retrieve().body(Product.class);
    }
}
