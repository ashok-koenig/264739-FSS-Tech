package com.example.products_api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotEmpty(message = "Product name is required")
    String title;
    //@NotEmpty(message = "Product category is required")
    @Enumerated(EnumType.STRING)
    Category category;
    @PositiveOrZero(message = "Product price is invalid")
    Double price;

    // Entity class should have a default constructor and getter / setter methods for all fields

    public Product() {
    }

    public Product(Long id, String title, Category category, Double price) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
