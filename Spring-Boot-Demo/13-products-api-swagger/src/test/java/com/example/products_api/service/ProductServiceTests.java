package com.example.products_api.service;

import com.example.products_api.model.Category;
import com.example.products_api.model.Product;
import com.example.products_api.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.mockito.Mockito.when;

public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts(){
        List<Product> mockProducts = List.of(
                new Product(1L,"iPhone 17", Category.Smartphone, 1789.0),
                new Product(2L, "HP Laptop", Category.Laptop, 7676.0)
        );
        when(productRepository.findAll()).thenReturn(mockProducts);
        List<Product> result = productService.getAllProducts();
        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(mockProducts.get(0).getTitle(), result.get(0).getTitle());
    }

    @Test
    void testSaveProduct(){
        Product mockProduct = new Product(1L,"iPhone 17", Category.Smartphone, 1789.0);
        when(productRepository.save(mockProduct)).thenReturn(mockProduct);

        Product result = productService.saveProduct(mockProduct);
        Assertions.assertEquals(mockProduct.getTitle(), result.getTitle());
    }

}
