package com.example.demo.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {

    @KafkaListener(topics = "demo-topic", groupId = "demo-group")
    public void listen(String message){
        System.out.println("Message Received: "+ message);
    }
}
