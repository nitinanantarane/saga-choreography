package com.example.orderservice.service;

import com.example.commonservice.dto.OrderRequestDTO;
import com.example.commonservice.event.OrderEvent;
import com.example.commonservice.event.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class OrderStatusPublisher {

    @Autowired
    Sinks.Many<OrderEvent> orderSinks;

    public void publishOrderStatus(OrderRequestDTO orderRequestDTO, OrderStatus orderStatus) {
        OrderEvent orderEvent = new OrderEvent(orderRequestDTO, orderStatus);
        orderSinks.tryEmitNext(orderEvent);
    }

}
