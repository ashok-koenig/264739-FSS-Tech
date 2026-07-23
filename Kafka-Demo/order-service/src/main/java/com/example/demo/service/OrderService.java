package com.example.demo.service;

import com.example.demo.model.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {
    private final KafkaTemplate<String, Order> kafkaTemplate;

    public OrderService(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void orderPlaced(Order order){
        kafkaTemplate.send("orders", order);
        System.out.println("Order Placed: "+ order.getId());
    }
}
