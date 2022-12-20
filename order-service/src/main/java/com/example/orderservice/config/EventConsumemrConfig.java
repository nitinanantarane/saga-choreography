package com.example.orderservice.config;

import com.example.commonservice.event.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventConsumemrConfig {

    @Autowired
    private OrderStatusUpdateHandler orderStatusUpdateHandler;

    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        return (paymentEvent) -> orderStatusUpdateHandler.updateOrder(
                paymentEvent.getPaymentRequestDTO().getOrderId(),
                purchaseOrder -> purchaseOrder.setPaymentStatus(paymentEvent.getPaymentStatus())
        );
    }
}
