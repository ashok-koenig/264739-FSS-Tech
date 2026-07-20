package com.example.mvc_demo.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

public class Employee {
    @NotEmpty(message = "Name is required")
    String name;
    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email")
    String email;
    @Positive(message = "Invalid salary")
    double salary;

    public Employee(){

    }

    public Employee(String name, String email, double salary) {
        this.name = name;
        this.email = email;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
