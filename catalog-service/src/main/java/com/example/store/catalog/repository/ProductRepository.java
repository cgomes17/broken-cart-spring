package com.example.store.catalog.repository;

import com.example.store.catalog.domain.Product;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Repository;

@Repository
public class ProductRepository {

    private final Map<String, Product> productsBySku;

    public ProductRepository() {
        this.productsBySku = Stream.of(
                        new Product("BOOK-1", "Clean Code", "books", new BigDecimal("39.90"),
                                Map.of("author", "Robert C. Martin", "pages", "464")),
                        new Product("BOOK-2", "Effective Java", "books", new BigDecimal("54.95"),
                                Map.of("author", "Joshua Bloch", "edition", "3")),
                        new Product("MUG-1", "Java Mug", "kitchen", new BigDecimal("14.50"),
                                Map.of("capacityMl", "350", "color", "black")),
                        new Product("TSHIRT-1", "Spring T-Shirt", "apparel", new BigDecimal("24.00"),
                                Map.of("size", "M", "color", "green")),
                        new Product("KEYB-1", "Mechanical Keyboard", "electronics", new BigDecimal("129.99"),
                                Map.of("layout", "US", "switch", "brown")))
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(Product::sku, Function.identity(), (a, b) -> a, LinkedHashMap::new),
                        Map::copyOf));
    }

    public List<Product> findAll() {
        return List.copyOf(productsBySku.values());
    }

    public List<Product> findByCategory(String category) {
        return productsBySku.values().stream().filter(p -> p.category().equals(category)).toList();
    }

    public Optional<Product> findBySku(String sku) {
        return Optional.ofNullable(productsBySku.get(sku));
    }

    public List<Product> findAllBySku(Collection<String> skus) {
        return skus.stream().distinct().map(productsBySku::get).filter(Objects::nonNull).toList();
    }
}
