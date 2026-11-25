package com.example.store.web.api.controller;

import com.example.store.web.integration.http.catalog.CatalogClient;
import com.example.store.web.integration.http.order.OrderClient;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StoreController {

    private final CatalogClient catalog;
    private final OrderClient orders;

    public StoreController(CatalogClient catalog, OrderClient orders) {
        this.catalog = catalog;
        this.orders = orders;
    }

    @GetMapping("/")
    public String products(@RequestParam(required = false) String category, Model model) {
        model.addAttribute("products", catalog.findAll(category));
        model.addAttribute("category", category);
        return "products";
    }

    @GetMapping("/orders/{id}")
    public String order(@PathVariable Long id, Model model) {
        model.addAttribute("order", orders.find(id));
        return "order";
    }
}
