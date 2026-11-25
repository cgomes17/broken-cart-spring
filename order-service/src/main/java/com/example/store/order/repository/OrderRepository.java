package com.example.store.order.repository;

import com.example.store.order.domain.Order;

import java.util.Optional;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private final JpaOrderRepository orders;

    public OrderRepository(JpaOrderRepository orders) {
        this.orders = orders;
    }

    public Order save(Order order) {
        return OrderMapper.toDomain(orders.save(OrderMapper.toEntity(order)));
    }

    public Optional<Order> findById(Long id) {
        return orders.findById(id).map(OrderMapper::toDomain);
    }

    public Optional<Order> findByIdempotencyKey(String idempotencyKey) {
        return orders.findByIdempotencyKey(idempotencyKey).map(OrderMapper::toDomain);
    }
}
