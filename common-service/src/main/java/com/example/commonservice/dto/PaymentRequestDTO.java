package com.example.commonservice.dto;

import com.example.commonservice.event.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {
    private Integer orderId;
    private Integer userId;
    private Integer amount;
}
