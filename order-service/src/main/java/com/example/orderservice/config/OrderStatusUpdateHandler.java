package com.example.orderservice.config;

import com.example.commonservice.dto.OrderRequestDTO;
import com.example.commonservice.event.OrderStatus;
import com.example.commonservice.event.PaymentStatus;
import com.example.orderservice.entity.PurchaseOrder;
import com.example.orderservice.repository.PurchaseOrderRepository;
import com.example.orderservice.service.OrderStatusPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import java.util.function.Consumer;

@Configuration
public class OrderStatusUpdateHandler {

    @Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    private OrderStatusPublisher orderStatusPublisher;

    @Transactional
    public void updateOrder(int id, Consumer<PurchaseOrder> purchaseOrderConsumer) {
        purchaseOrderRepository.findById(id).ifPresent(purchaseOrderConsumer.andThen(this::updateOrder));
    }

    private void updateOrder(PurchaseOrder purchaseOrder) {
        boolean isPaymentComplete = PaymentStatus.PAYMENT_COMPLETED.equals(purchaseOrder.getPaymentStatus());
        OrderStatus orderStatus = isPaymentComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCLLED;
        purchaseOrder.setOrderStatus(orderStatus);
        if(!isPaymentComplete) {
            orderStatusPublisher.publishOrderStatus(convertyEntityToDto(purchaseOrder), orderStatus);
        }
    }

    private OrderRequestDTO convertyEntityToDto(PurchaseOrder purchaseOrder) {
        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setUserId(purchaseOrder.getUserId());
        orderRequestDTO.setOrderId(purchaseOrder.getId());
        orderRequestDTO.setProductId(orderRequestDTO.getProductId());
        orderRequestDTO.setAmount(purchaseOrder.getAmount());
        return orderRequestDTO;
    }
}
