package com.example.store.web.integration.http.order;

import java.math.BigDecimal;
import java.util.List;

public record OrderView(Long id, String customerId, String customerType, String status, BigDecimal total,
                        List<Line> lines) {

    public OrderView {
        lines = List.copyOf(lines);
    }

    public record Line(String sku, String name, BigDecimal unitPrice, int quantity) {
    }
}
