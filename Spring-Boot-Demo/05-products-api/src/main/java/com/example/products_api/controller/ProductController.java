package com.example.products_api.controller;

import com.example.products_api.model.Product;
import com.example.products_api.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductController {
    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product){
        return productService.saveProduct(product);
    }

    @GetMapping
    public List<Product> allProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getAProduct(@PathVariable Long id){
        return productService.getProductById(id);
    }
}
