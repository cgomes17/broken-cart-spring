package com.example.store.order.api.controller;

import com.example.store.order.api.model.CreateOrderRequest;
import com.example.store.order.api.model.OrderResponse;

import com.example.store.order.domain.exception.ProductNotFoundException;
import com.example.store.order.service.OrderService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders/create")
    public OrderResponse create(@RequestBody CreateOrderRequest request) throws ProductNotFoundException {
        return OrderResponse.from(orderService.create(request));
    }

    @GetMapping("/orders/{id}")
    public OrderResponse find(@PathVariable Long id) {
        return OrderResponse.from(orderService.find(id));
    }
}
