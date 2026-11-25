package com.example.store.order.domain;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private final Long id;
    private final String customerId;
    private final String customerType;
    private final String status;
    private BigDecimal total;
    private final String idempotencyKey;
    private final Instant createdAt;
    private final List<OrderItem> items;

    public Order(String customerId, String customerType, String idempotencyKey) {
        this(null, customerId, customerType, "CREATED",
                new BigDecimal("0.00"), idempotencyKey, Instant.now(), List.of());
    }

    private Order(Long id, String customerId, String customerType, String status, BigDecimal total,
                  String idempotencyKey, Instant createdAt, List<OrderItem> items) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId is required");
        }
        this.id = id;
        this.customerId = customerId;
        this.customerType = customerType;
        this.status = Objects.requireNonNull(status);
        this.total = Objects.requireNonNull(total);
        this.idempotencyKey = idempotencyKey;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.items = new ArrayList<>(List.copyOf(items));
    }

    public static Order restore(Long id, String customerId, String customerType, String status, BigDecimal total,
                                String idempotencyKey, Instant createdAt, List<OrderItem> items) {
        return new Order(id, customerId, customerType, status, total, idempotencyKey, createdAt, items);
    }

    public void addItem(OrderItem item) {
        items.add(Objects.requireNonNull(item));
        calculateTotal();
    }

    private void calculateTotal() {
        BigDecimal subtotal = items.stream().map(OrderItem::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        this.total = Pricing.total(customerType, subtotal);
    }

    public Long getId() {
        return id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }
}
