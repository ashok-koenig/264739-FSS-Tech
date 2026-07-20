package com.example.code_verification.controller;

import com.example.code_verification.service.CodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CodeController {
    private CodeService codeService;

    public CodeController(CodeService codeService) {
        this.codeService = codeService;
    }

    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping("/generate")
    public String generate(Model model){
        String code = codeService.generate();
        model.addAttribute("code", code);
        return "generate";
    }

    @RequestMapping("/validate-form")
    public String validateForm(Model model){
        return "validate-form";
    }

    @RequestMapping("/validate-code")
    public String validateCode(@RequestParam(required = true) String code, Model model){
        boolean result = codeService.validate(code);
        model.addAttribute("result", result);
        return "validate-code";
    }
}
