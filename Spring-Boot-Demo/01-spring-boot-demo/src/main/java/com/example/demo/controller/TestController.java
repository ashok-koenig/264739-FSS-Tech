package com.example.demo.controller;

import com.example.demo.service.CalculateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired // Method 2: Field Injection
    private CalculateService calculateService;
    // Dependency Injection - Method 1: Constructor Injection
//    public TestController(CalculateService calculateService){
//        this.calculateService = calculateService;
//    }

    @RequestMapping("/")
    public String welcome(){
        return "Welcome to Spring Boot";
    }

    @RequestMapping("/total")
    public int total(){
//        CalculateService calculateService = new CalculateService();
        int result = calculateService.sum(10, 20);
        return result;
    }
}
