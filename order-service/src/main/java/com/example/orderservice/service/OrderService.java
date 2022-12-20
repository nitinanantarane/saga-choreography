package com.example.orderservice.service;

import com.example.commonservice.dto.OrderRequestDTO;
import com.example.commonservice.dto.OrderResponseDTO;
import com.example.commonservice.event.OrderStatus;
import com.example.orderservice.entity.PurchaseOrder;
import com.example.orderservice.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private OrderStatusPublisher orderStatusPublisher;
    public PurchaseOrder createOrder(OrderRequestDTO orderRequestDTO) {
        PurchaseOrder order = purchaseOrderRepository.save(convertDtoToEntity(orderRequestDTO));
        orderRequestDTO.setOrderId(order.getId());
        orderStatusPublisher.publishOrderStatus(orderRequestDTO, OrderStatus.ORDER_CREATED);
        return order;
    }

    public List<PurchaseOrder> findAll() {
        return purchaseOrderRepository.findAll();
    }


    private PurchaseOrder convertDtoToEntity(OrderRequestDTO orderRequestDTO) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUserId(orderRequestDTO.getUserId());
        purchaseOrder.setAmount(orderRequestDTO.getAmount());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        return purchaseOrder;
    }
}
