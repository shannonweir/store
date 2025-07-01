package com.example.store.cache;

import com.example.store.repository.ProductRepository;
import com.example.store.service.ProductService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductCacheTest {

    @Autowired
    private ProductService productService;

    @MockitoSpyBean
    private ProductRepository productRepository;

    @Test
    void shouldCacheAllProducts() {
        productService.getAllProducts();
        productService.getAllProducts();
        verify(productRepository, times(1)).findAll();
    }
}
