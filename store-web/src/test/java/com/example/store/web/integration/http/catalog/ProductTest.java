package com.example.store.web.integration.http.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

class ProductTest {

    @Test
    void rejectsMalformedCatalogData() {
        assertThrows(IllegalArgumentException.class, () -> new Product(null, "Book", "books", BigDecimal.ONE, null));
        assertThrows(IllegalArgumentException.class, () -> new Product(" ", "Book", "books", BigDecimal.ONE, null));
        assertThrows(IllegalArgumentException.class, () -> new Product("BOOK-1", null, "books", BigDecimal.ONE, null));
        assertThrows(IllegalArgumentException.class, () -> new Product("BOOK-1", " ", "books", BigDecimal.ONE, null));
        assertThrows(IllegalArgumentException.class, () -> new Product("BOOK-1", "Book", null, BigDecimal.ONE, null));
        assertThrows(IllegalArgumentException.class, () -> new Product("BOOK-1", "Book", " ", BigDecimal.ONE, null));
        assertThrows(IllegalArgumentException.class, () -> new Product("BOOK-1", "Book", "books", null, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Product("BOOK-1", "Book", "books", BigDecimal.ONE.negate(), null));
    }

    @Test
    void copiesOptionalAttributes() {
        Map<String, String> attributes = new HashMap<>(Map.of("author", "Author"));
        Product product = new Product("BOOK-1", "Book", "books", BigDecimal.ZERO, attributes);
        attributes.clear();

        assertEquals(Map.of("author", "Author"), product.attributes());
        assertThrows(UnsupportedOperationException.class, () -> product.attributes().clear());
        assertEquals(Map.of(), new Product("BOOK-1", "Book", "books", BigDecimal.ZERO, null).attributes());
    }
}
