package com.example.code_verification.service;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CodeService {
    private SecureRandom random = new SecureRandom();

    // store last generated code
    private String lastGeneratedCode;

    public String generate(){
        lastGeneratedCode = String.valueOf(10000 + random.nextInt(90000));
        return lastGeneratedCode;
    }

    public boolean validate(String inputCode){
        if(inputCode == null){
            return false;
        }
        if(!lastGeneratedCode.equals(inputCode)){
            return  false;
        }
        return true;
    }
}
