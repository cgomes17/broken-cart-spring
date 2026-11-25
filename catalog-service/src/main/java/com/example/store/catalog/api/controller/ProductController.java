package com.example.store.catalog.api.controller;

import com.example.store.catalog.api.model.ProductResponse;
import com.example.store.catalog.repository.ProductRepository;

import java.time.Duration;
import java.util.List;

import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository products;

    public ProductController(ProductRepository products) {
        this.products = products;
    }

    @GetMapping
    public List<ProductResponse> search(@RequestParam(required = false) String category,
                                @RequestParam(name = "sku", required = false) List<String> skus) {
        if (skus != null && !skus.isEmpty()) {
            return products.findAllBySku(skus).stream().map(ProductResponse::from).toList();
        }
        if (category != null && !category.isBlank()) {
            return products.findByCategory(category).stream().map(ProductResponse::from).toList();
        }
        return products.findAll().stream().map(ProductResponse::from).toList();
    }

    @GetMapping("/{sku}")
    public ResponseEntity<ProductResponse> findBySku(@PathVariable String sku) {
        return products.findBySku(sku)
                .map(ProductResponse::from)
                .map(product -> ResponseEntity.ok()
                        .cacheControl(CacheControl.maxAge(Duration.ofMinutes(5)))
                        .body(product))
                .orElse(ResponseEntity.notFound().build());
    }
}
