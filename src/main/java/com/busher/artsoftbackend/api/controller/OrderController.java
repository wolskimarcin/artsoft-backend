package com.busher.artsoftbackend.api.controller;

import com.busher.artsoftbackend.model.LocalUser;
import com.busher.artsoftbackend.model.WebOrder;
import com.busher.artsoftbackend.service.WebOrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final WebOrderService orderService;

    public OrderController(WebOrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<WebOrder> getOrders(@AuthenticationPrincipal LocalUser user) {
        return orderService.getWebOrders(user);
    }

}