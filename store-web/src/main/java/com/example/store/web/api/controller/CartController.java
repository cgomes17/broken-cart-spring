package com.example.store.web.api.controller;

import com.example.store.web.integration.http.order.OrderView;
import com.example.store.web.integration.http.order.PlaceOrderRequest;
import com.example.store.web.integration.http.catalog.Product;
import com.example.store.web.domain.Cart;
import com.example.store.web.integration.http.catalog.CatalogClient;
import com.example.store.web.integration.http.order.OrderClient;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final Cart cart = new Cart();
    private final CatalogClient catalog;
    private final OrderClient orders;

    public CartController(CatalogClient catalog, OrderClient orders) {
        this.catalog = catalog;
        this.orders = orders;
    }

    @GetMapping
    public String view(Model model) {
        model.addAttribute("cart", cart);
        model.addAttribute("idempotencyKey", UUID.randomUUID().toString());
        return "cart";
    }

    @PostMapping("/items")
    public String add(@RequestParam String sku) {
        Product product = catalog.findBySku(sku);
        cart.add(product.sku(), product.name(), product.price());
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout(@RequestParam String customerId,
                           @RequestParam String customerType,
                           @RequestParam String idempotencyKey,
                           RedirectAttributes redirect) {
        if (cart.isEmpty()) {
            redirect.addFlashAttribute("error", "Your cart is empty.");
            return "redirect:/cart";
        }
        List<PlaceOrderRequest.Line> lines = cart.items().stream()
                .map(item -> new PlaceOrderRequest.Line(item.sku(), item.quantity()))
                .toList();
        try {
            OrderView order = orders.create(new PlaceOrderRequest(customerId, customerType, lines), idempotencyKey);
            cart.clear();
            return "redirect:/orders/" + order.id();
        } catch (RestClientResponseException e) {
            redirect.addFlashAttribute("error", "Checkout failed (" + e.getStatusCode().value() + ").");
            return "redirect:/cart";
        } catch (IllegalArgumentException e) {
            redirect.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }
}
