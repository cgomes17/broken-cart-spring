package com.example.store.web.api.controller;

import com.example.store.web.integration.http.status.ServiceStatusClient;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StatusController {

    private final ServiceStatusClient status;

    public StatusController(ServiceStatusClient status) {
        this.status = status;
    }

    @GetMapping("/status")
    public String status(Model model) {
        Map<String, Boolean> services = status.check();
        model.addAttribute("services", services);
        model.addAttribute("allUp", services.values().stream().allMatch(up -> up));
        return "status";
    }
}
