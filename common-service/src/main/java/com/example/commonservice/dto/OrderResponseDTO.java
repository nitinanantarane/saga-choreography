package com.example.commonservice.dto;

import com.example.commonservice.event.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDTO {
    private String userId;
    private String orderId;
    private String productId;
    private Integer amount;
    private OrderStatus orderStatus;
}
