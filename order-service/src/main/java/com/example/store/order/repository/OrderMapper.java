package com.example.store.order.repository;

import com.example.store.order.domain.Order;
import com.example.store.order.domain.OrderItem;

final class OrderMapper {

    private OrderMapper() {
    }

    static OrderEntity toEntity(Order order) {
        OrderEntity entity = new OrderEntity();
        entity.id = order.getId();
        entity.customerId = order.getCustomerId();
        entity.customerType = order.getCustomerType();
        entity.status = order.getStatus();
        entity.total = order.getTotal();
        entity.idempotencyKey = order.getIdempotencyKey();
        entity.createdAt = order.getCreatedAt();
        for (OrderItem item : order.getItems()) {
            OrderItemEntity line = new OrderItemEntity();
            line.id = item.getId();
            line.order = entity;
            line.sku = item.getSku();
            line.name = item.getName();
            line.unitPrice = item.getUnitPrice();
            line.quantity = item.getQuantity();
            entity.items.add(line);
        }
        return entity;
    }

    static Order toDomain(OrderEntity entity) {
        return Order.restore(entity.id, entity.customerId, entity.customerType, entity.status, entity.total,
                entity.idempotencyKey, entity.createdAt, entity.items.stream()
                        .map(item -> OrderItem.restore(item.id, item.sku, item.name, item.unitPrice, item.quantity))
                        .toList());
    }
}
