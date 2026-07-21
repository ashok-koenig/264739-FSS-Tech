package com.example.products_api.service;

import com.example.products_api.exception.ProductNotFoundException;
import com.example.products_api.model.Product;
import com.example.products_api.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private ProductRepository productRepository;
    // DI
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    // Same for create and update product
    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    public Page<Product> getAllProducts(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }
}
