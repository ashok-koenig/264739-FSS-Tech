package com.example.mvc_demo.controller;

import com.example.mvc_demo.model.Employee;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class EmployeeController {
    @RequestMapping("/form")
    public String showForm(Model model){
        model.addAttribute("employee", new Employee());
        return "form";
    }

    @RequestMapping("/save")
    public String save(@ModelAttribute Employee employee, Model model){
        model.addAttribute("employee", employee);
        return "save";
    }
}
