package com.example.store.catalog.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

class ProductRepositoryTest {

    private final ProductRepository repository = new ProductRepository();

    @Test
    void findsBySkuAndCategory() {
        assertTrue(repository.findBySku("BOOK-1").isPresent());
        assertTrue(repository.findBySku("NOPE").isEmpty());
        assertEquals(2, repository.findByCategory("books").size());
    }

    @Test
    void batchLookupIgnoresUnknownAndDuplicateSkus() {
        assertEquals(2, repository.findAllBySku(List.of("BOOK-1", "MUG-1", "BOOK-1", "NOPE")).size());
    }

    @Test
    void returnedCollectionsAreImmutable() {
        assertThrows(UnsupportedOperationException.class, () -> repository.findAll().clear());
    }
}
