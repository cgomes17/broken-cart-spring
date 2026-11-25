package com.example.store.web.integration.http.order;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class OrderClient {

    private final RestClient restClient;

    public OrderClient(RestClient.Builder builder, @Value("${store.order-url}") String baseUrl) {
        this.restClient = builder.baseUrl(baseUrl).build();
    }

    public OrderView create(PlaceOrderRequest request, String idempotencyKey) {
        return restClient.post()
                .uri("/orders/create")
                .header("Idempotency-Key", idempotencyKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(OrderView.class);
    }

    public OrderView find(Long id) {
        return restClient.get().uri("/orders/{id}", id).retrieve().body(OrderView.class);
    }
}
