package com.example.products_api.service;

import com.example.products_api.exception.ProductNotFoundException;
import com.example.products_api.model.Product;
import com.example.products_api.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    @CacheEvict(value = "products", allEntries = true)
    public Product saveProduct(Product product){
        return productRepository.save(product);
    }

    @Cacheable(value = "products")
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
    }

    @CacheEvict(value = "products", allEntries = true)
    public void deleteProductById(Long id){
        productRepository.deleteById(id);
    }
}
