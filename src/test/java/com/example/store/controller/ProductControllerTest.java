package com.example.store.controller;

import com.example.store.dto.ProductDTO;
import com.example.store.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDTO product1;
    private ProductDTO product2;

    @BeforeEach
    void setUp() {
        product1 = ProductDTO.builder().id(1L).description("Product 1").build();

        product2 = ProductDTO.builder().id(2L).description("Product 2").build();
    }

    @Test
    void testGetAllProducts() throws Exception {
        Mockito.when(productService.getAllProducts()).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Product 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].description").value("Product 2"));
    }

    @Test
    void testGetProductById_Found() throws Exception {
        Mockito.when(productService.getProductById(1L)).thenReturn(Optional.of(product1));

        mockMvc.perform(get("/product/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Product 1"));
    }

    @Test
    void testGetProductById_NotFound() throws Exception {
        Mockito.when(productService.getProductById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/product/999"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Product with ID 999 not found"));
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductDTO requestDto = ProductDTO.builder().description("New Product").build();

        ProductDTO savedDto =
                ProductDTO.builder().id(10L).description("New Product").build();

        Mockito.when(productService.createProduct(any(ProductDTO.class))).thenReturn(savedDto);

        mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.description").value("New Product"));
    }
}
