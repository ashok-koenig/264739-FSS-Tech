package com.example.demo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @GetMapping
    public String getAllUsers(){
        return "List of users";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable int id){
        return "User with id: "+ id;
    }

    @PostMapping
    public String createUser(@RequestBody String name){
        return "User created with name: "+ name;
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable int id, @RequestBody String name){
        return "User updated with name: "+ name;
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id){
        return "User deleted with id: "+ id;
    }
}
