package com.example.store.web.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Cart {

    private final List<CartLine> lines = new ArrayList<>();

    public void add(String sku, String name, BigDecimal unitPrice) {
        CartLine line = new CartLine(sku, name, unitPrice);
        int index = lines.indexOf(line);
        if (index >= 0) {
            lines.get(index).increase();
        } else {
            line.increase();
            lines.add(line);
        }
    }

    public List<CartItem> items() {
        return lines.stream()
                .map(l -> new CartItem(l.sku(), l.name(), l.unitPrice(), l.quantity()))
                .toList();
    }

    public BigDecimal total() {
        return items().stream().map(CartItem::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public String summary() {
        return "";
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public void clear() {
        lines.clear();
    }
}
