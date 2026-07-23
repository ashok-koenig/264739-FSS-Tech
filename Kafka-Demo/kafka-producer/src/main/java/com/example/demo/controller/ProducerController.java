package com.example.demo.controller;

import com.example.demo.service.ProducerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {
    private final ProducerService producerService;

    public ProducerController(ProducerService producerService) {
        this.producerService = producerService;
    }

    @GetMapping("/produce")
    public String send(@RequestParam String msg){
        producerService.sendMessage(msg);
        return "Message Sent: "+ msg;
    }
}
