package com.example.orderservice.controller;

import com.example.commonservice.dto.OrderRequestDTO;
import com.example.commonservice.dto.OrderResponseDTO;
import com.example.orderservice.entity.PurchaseOrder;
import com.example.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public PurchaseOrder create(@RequestBody OrderRequestDTO orderRequestDTO) {
        return orderService.createOrder(orderRequestDTO);
    }

    @GetMapping
    public List<PurchaseOrder> getAllOrders() {
        return orderService.findAll();
    }
}
