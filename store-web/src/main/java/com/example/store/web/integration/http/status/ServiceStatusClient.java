package com.example.store.web.integration.http.status;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceStatusClient {

    private final Map<String, String> services = new LinkedHashMap<>();
    private final HttpClient http = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(1)).build();

    public ServiceStatusClient(@Value("${store.catalog-url}") String catalogUrl,
                               @Value("${store.order-url}") String orderUrl) {
        services.put("catalog-service", catalogUrl);
        services.put("order-service", orderUrl);
    }

    public Map<String, Boolean> check() {
        Map<String, Boolean> result = new LinkedHashMap<>();
        services.forEach((name, url) -> result.put(name, isUp(url)));
        return result;
    }

    private boolean isUp(String baseUrl) {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/actuator/health"))
                    .timeout(Duration.ofSeconds(2)).GET().build();
            return http.send(request, HttpResponse.BodyHandlers.discarding()).statusCode() / 100 == 2;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
