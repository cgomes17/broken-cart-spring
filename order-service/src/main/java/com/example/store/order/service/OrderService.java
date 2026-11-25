package com.example.store.order.service;

import com.example.store.order.api.model.CreateOrderRequest;
import com.example.store.order.api.model.OrderLineRequest;
import com.example.store.order.integration.http.catalog.CatalogProduct;
import com.example.store.order.domain.Order;
import com.example.store.order.domain.OrderItem;
import com.example.store.order.domain.exception.ProductNotFoundException;
import com.example.store.order.integration.http.catalog.CatalogClient;
import com.example.store.order.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class OrderService {

    private final OrderRepository orders;
    private final CatalogClient catalog = new CatalogClient("http://localhost:8081");

    public OrderService(OrderRepository orders) {
        this.orders = orders;
    }

    public Order create(CreateOrderRequest request) throws ProductNotFoundException {
        Order order = new Order(request.customerId(), request.customerType(), null);
        for (OrderLineRequest line : request.lines()) {
            CatalogProduct product;
            try {
                product = catalog.findBySku(line.sku());
            } catch (HttpClientErrorException.NotFound e) {
                throw new ProductNotFoundException(line.sku());
            }
            order.addItem(new OrderItem(product.sku(), product.name(), product.price(), line.quantity()));
        }
        return orders.save(order);
    }

    public Order find(Long id) {
        return orders.findById(id).orElseThrow();
    }
}
