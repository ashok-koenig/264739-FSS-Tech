package com.example.demo.service;

import com.example.demo.model.Order;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer {
    @KafkaListener(topics = "orders", groupId = "notification-group")
    public void listen(Order order){
        System.out.println("Order Received: "+ order);
    }
}
