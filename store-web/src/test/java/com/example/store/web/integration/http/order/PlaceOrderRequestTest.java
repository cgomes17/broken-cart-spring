package com.example.store.web.integration.http.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class PlaceOrderRequestTest {

    private final PlaceOrderRequest.Line line = new PlaceOrderRequest.Line("BOOK-1", 1);

    @Test
    void validatesCustomerAndLines() {
        assertThrows(IllegalArgumentException.class, () -> new PlaceOrderRequest(null, null, List.of(line)));
        assertThrows(IllegalArgumentException.class, () -> new PlaceOrderRequest(" ", null, List.of(line)));
        assertThrows(IllegalArgumentException.class, () -> new PlaceOrderRequest("alice", null, null));
        assertThrows(IllegalArgumentException.class, () -> new PlaceOrderRequest("alice", null, List.of()));
        assertThrows(IllegalArgumentException.class,
                () -> new PlaceOrderRequest("alice", null, Arrays.asList(line, null)));
        assertThrows(IllegalArgumentException.class, () -> new PlaceOrderRequest.Line(null, 1));
        assertThrows(IllegalArgumentException.class, () -> new PlaceOrderRequest.Line(" ", 1));
        assertThrows(IllegalArgumentException.class, () -> new PlaceOrderRequest.Line("BOOK-1", 0));
        assertThrows(IllegalArgumentException.class, () -> new PlaceOrderRequest.Line("BOOK-1", -1));
    }

    @Test
    void defaultsCustomerTypeAndCopiesLines() {
        List<PlaceOrderRequest.Line> lines = new ArrayList<>(List.of(line));
        PlaceOrderRequest request = new PlaceOrderRequest("alice", null, lines);
        lines.clear();

        assertEquals("REGULAR", request.customerType());
        assertEquals(List.of(line), request.lines());
        assertThrows(UnsupportedOperationException.class, () -> request.lines().clear());
    }
}
