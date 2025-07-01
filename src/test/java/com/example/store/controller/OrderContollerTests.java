package com.example.store.controller;

import com.example.store.dto.CreateOrderDTO;
import com.example.store.dto.CustomerDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.dto.ProductDTO;
import com.example.store.mapper.CustomerMapper;
import com.example.store.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ComponentScan(basePackageClasses = CustomerMapper.class)
@RequiredArgsConstructor
class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderService;

    private OrderDTO order;
    private CustomerDTO customer;
    private CreateOrderDTO createOrder;
    private ProductDTO product;

    @BeforeEach
    void setUp() {
        customer = new CustomerDTO();
        customer.setName("John Doe");
        customer.setId(1L);

        product = ProductDTO.builder().id(1L).description("Test Product").build();

        order = new OrderDTO();
        order.setDescription("Test Order");
        order.setId(1L);
        order.setCustomer(customer);
        order.setProducts(List.of(product));

        createOrder = new CreateOrderDTO();
        createOrder.setDescription("Test Order");
        createOrder.setCustomerId(customer.getId());
        createOrder.setProducts(List.of(1L));
    }

    @Test
    void testCreateOrder() throws Exception {
        when(orderService.createOrder(createOrder)).thenReturn(order);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createOrder)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description").value("Test Order"))
                .andExpect(jsonPath("$.customer.name").value("John Doe"));
    }

    @Test
    void testGetOrder() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(order));

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Test Order"))
                .andExpect(jsonPath("$..customer.name").value("John Doe"));
    }
}
