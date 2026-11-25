package com.example.store.web.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartTest {

    private static final BigDecimal PRICE = new BigDecimal("39.90");

    private final Cart cart = new Cart();

    @Test
    @DisplayName("Task 1: adding the same product twice gives one line with quantity 2")
    void sameSkuFromLiteralsIsMerged() {
        cart.add("BOOK-1", "Clean Code", PRICE);
        cart.add("BOOK-1", "Clean Code", PRICE);

        assertEquals(1, cart.items().size());
        assertEquals(2, cart.items().getFirst().quantity());
    }

    @Test
    @DisplayName("Task 1: the same product coming from a request is also merged")
    void sameSkuFromHttpIsMerged() {
        cart.add(new String("BOOK-1"), "Clean Code", PRICE);
        cart.add(new String("BOOK-1"), "Clean Code", PRICE);

        assertEquals(1, cart.items().size());
        assertEquals(2, cart.items().getFirst().quantity());
    }

    @Test
    @DisplayName("Task 2: summary() lists each line as \"<qty> x <name>\", separated by commas")
    void summaryListsQuantitiesAndNames() {
        cart.add("BOOK-1", "Clean Code", PRICE);
        cart.add("BOOK-1", "Clean Code", PRICE);
        cart.add("MUG-1", "Java Mug", new BigDecimal("14.50"));

        assertEquals("2 x Clean Code, 1 x Java Mug", cart.summary());
    }

    @Test
    void rejectsInvalidCartLines() {
        assertThrows(IllegalArgumentException.class, () -> cart.add(null, "Book", PRICE));
        assertThrows(IllegalArgumentException.class, () -> new CartLine(null, "Book", PRICE));
        assertThrows(IllegalArgumentException.class, () -> new CartLine(" ", "Book", PRICE));
        assertThrows(IllegalArgumentException.class, () -> new CartLine("BOOK-1", null, PRICE));
        assertThrows(IllegalArgumentException.class, () -> new CartLine("BOOK-1", " ", PRICE));
        assertThrows(IllegalArgumentException.class, () -> new CartLine("BOOK-1", "Book", null));
        assertThrows(IllegalArgumentException.class, () -> new CartLine("BOOK-1", "Book", PRICE.negate()));
    }

    @Test
    void validatesSnapshotsAndCalculatesTheirTotals() {
        assertThrows(IllegalArgumentException.class, () -> new CartItem(null, "Book", PRICE, 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem(" ", "Book", PRICE, 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem("BOOK-1", null, PRICE, 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem("BOOK-1", " ", PRICE, 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem("BOOK-1", "Book", null, 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem("BOOK-1", "Book", PRICE.negate(), 1));
        assertThrows(IllegalArgumentException.class, () -> new CartItem("BOOK-1", "Book", PRICE, 0));
        assertThrows(IllegalArgumentException.class, () -> new CartItem("BOOK-1", "Book", PRICE, -1));
        assertEquals(new BigDecimal("79.80"), new CartItem("BOOK-1", "Book", PRICE, 2).lineTotal());
    }
}
