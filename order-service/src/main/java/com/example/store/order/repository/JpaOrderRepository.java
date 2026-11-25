package com.example.store.order.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface JpaOrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByIdempotencyKey(String idempotencyKey);
}
