package com.example.code_verification.controller;

import com.example.code_verification.service.CodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class CodeController {
    private CodeService codeService;
    private static Logger logger = LoggerFactory.getLogger(CodeController.class);

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
        logger.info("Generated code: "+ code);
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
        if(result){
            logger.info("Validation Success");
        }else{
            logger.warn("Validation Failed with code: "+ code);
        }
        return "validate-code";
    }
}
