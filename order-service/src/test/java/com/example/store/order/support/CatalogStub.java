package com.example.store.order.support;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CatalogStub implements AutoCloseable {

    private static final Map<String, String> PRODUCTS = Map.of(
            "BOOK-1", "{\"sku\":\"BOOK-1\",\"name\":\"Clean Code\",\"price\":39.90}",
            "BOOK-2", "{\"sku\":\"BOOK-2\",\"name\":\"Effective Java\",\"price\":54.95}",
            "MUG-1", "{\"sku\":\"MUG-1\",\"name\":\"Java Mug\",\"price\":14.50}");

    private final HttpServer server;
    private final AtomicInteger requests = new AtomicInteger();

    public CatalogStub() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 8081), 0);
        server.createContext("/products", this::handle);
        server.start();
    }

    public int requestCount() {
        return requests.get();
    }

    private void handle(HttpExchange exchange) throws IOException {
        requests.incrementAndGet();
        String path = exchange.getRequestURI().getPath();
        String query = exchange.getRequestURI().getQuery();
        if (path.equals("/products")) {
            List<String> skus = query == null ? List.of()
                    : List.of(query.split("&")).stream().filter(p -> p.startsWith("sku="))
                            .map(p -> p.substring(4)).toList();
            respond(exchange, 200, "[" + skus.stream().filter(PRODUCTS::containsKey).map(PRODUCTS::get)
                    .collect(Collectors.joining(",")) + "]");
            return;
        }
        String body = PRODUCTS.get(path.substring("/products/".length()));
        if (body == null) {
            respond(exchange, 404, "");
        } else {
            respond(exchange, 200, body);
        }
    }

    private void respond(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length == 0 ? -1 : bytes.length);
        if (bytes.length > 0) {
            exchange.getResponseBody().write(bytes);
        }
        exchange.close();
    }

    @Override
    public void close() {
        server.stop(0);
    }
}
