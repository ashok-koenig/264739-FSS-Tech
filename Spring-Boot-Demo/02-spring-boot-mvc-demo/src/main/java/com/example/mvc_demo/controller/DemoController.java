package com.example.mvc_demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/")
    public String home(){
        return "home";
    }

    @RequestMapping("/about")
    public String about(Model model){
        model.addAttribute("first_name", "John");
        model.addAttribute("last_name", "Smith");
        model.addAttribute("age", 15);
        String[] friends = {"Peter", "Bob", "Ana", "Smith"};
        model.addAttribute("friends", friends);
        return "about";
    }
}
