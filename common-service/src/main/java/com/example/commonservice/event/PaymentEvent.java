package com.example.commonservice.event;

import com.example.commonservice.dto.PaymentRequestDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Data
public class PaymentEvent implements Event {
    private UUID eventId = UUID.randomUUID();
    private Date eventDate = new Date();
    private PaymentRequestDTO paymentRequestDTO;
    private PaymentStatus paymentStatus;

    public PaymentEvent(PaymentRequestDTO paymentRequestDTO, PaymentStatus paymentStatus) {
        this.paymentRequestDTO = paymentRequestDTO;
        this.paymentStatus = paymentStatus;
    }

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getEventDate() {
        return eventDate;
    }
}
