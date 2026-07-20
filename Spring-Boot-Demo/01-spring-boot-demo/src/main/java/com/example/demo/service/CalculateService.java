package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class CalculateService {
    public int sum(int n1, int n2){
        return n1 + n2;
    }
}
