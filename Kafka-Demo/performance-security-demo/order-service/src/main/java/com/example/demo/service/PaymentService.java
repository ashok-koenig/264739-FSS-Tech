package com.example.demo.service;

import com.example.demo.model.Payment;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final KafkaTemplate<String, Payment> kafkaTemplate;

    public PaymentService(KafkaTemplate<String, Payment> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void paymentMade(Payment payment){
        kafkaTemplate.send("payments", String.valueOf(payment.getOrderId()), payment);
        System.out.println("Payment Made: "+ payment.getOrderId());
    }
}
