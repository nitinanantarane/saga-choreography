package com.example.paymentservice.service;

import com.example.commonservice.dto.OrderRequestDTO;
import com.example.commonservice.dto.PaymentRequestDTO;
import com.example.commonservice.event.OrderEvent;
import com.example.commonservice.event.PaymentEvent;
import com.example.commonservice.event.PaymentStatus;
import com.example.paymentservice.entity.UserBalance;
import com.example.paymentservice.entity.UserTransaction;
import com.example.paymentservice.repository.UserBalanceRepository;
import com.example.paymentservice.repository.UserTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PaymentService {

    @Autowired
    private UserBalanceRepository userBalanceRepository;

    @Autowired
    private UserTransactionRepository userTransactionRepository;

    @PostConstruct
    public void initUserBalance() {
        userBalanceRepository.saveAll(Stream.of(new UserBalance(101, 5000),
                new UserBalance(102, 3000),
                new UserBalance(103, 2500),
                new UserBalance(104, 999),
                new UserBalance(105, 4000)).collect(Collectors.toList()));
    }

    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        OrderRequestDTO orderRequestDTO = orderEvent.getOrderRequestDTO();
        PaymentRequestDTO paymentRequestDTO = new PaymentRequestDTO(orderRequestDTO.getOrderId(),
                orderRequestDTO.getUserId(), orderRequestDTO.getAmount());

        return userBalanceRepository.findById(orderRequestDTO.getUserId())
                .filter(ub-> ub.getAmount() > orderRequestDTO.getAmount())
                .map(ub -> {
                    ub.setAmount(ub.getAmount() - orderRequestDTO.getAmount());
                    userTransactionRepository.save(new UserTransaction(orderRequestDTO.getOrderId(),
                            orderRequestDTO.getUserId(), orderRequestDTO.getAmount()));
                    return new PaymentEvent(paymentRequestDTO, PaymentStatus.PAYMENT_COMPLETED);
                }).orElse(new PaymentEvent(paymentRequestDTO, PaymentStatus.PAYMENT_FAILED));
    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {
        userTransactionRepository.findById(orderEvent.getOrderRequestDTO().getOrderId())
                .ifPresent(ut -> {
                    userTransactionRepository.delete(ut);
                    userBalanceRepository.findById(orderEvent.getOrderRequestDTO().getUserId())
                            .ifPresent(ub -> ub.setAmount(ub.getAmount() + ut.getAmount()));
                });
    }
}
