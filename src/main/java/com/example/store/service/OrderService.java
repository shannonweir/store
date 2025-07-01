package com.example.store.service;

import com.example.store.dto.CreateOrderDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Customer;
import com.example.store.entity.Order;
import com.example.store.entity.Product;
import com.example.store.mapper.OrderMapper;
import com.example.store.repository.CustomerRepository;
import com.example.store.repository.OrderRepository;
import com.example.store.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;

    @Cacheable("orders")
    public List<OrderDTO> getAllOrders() {
        return orderMapper.toOrderDtos(orderRepository.findAll());
    }

    @Cacheable(value = "orders", key = "#id")
    public Optional<OrderDTO> getOrderById(Long id) {
        return orderRepository.findById(id).map(orderMapper::toOrderDto);
    }

    @Transactional
    @CacheEvict(value = "orders", allEntries = true)
    public OrderDTO createOrder(CreateOrderDTO orderDTO) {
        if (orderDTO.getCustomerId() == null) {
            throw new IllegalArgumentException("Customer is required for creating an order");
        }

        Order order = orderMapper.toOrder(orderDTO);
        Customer customer = customerRepository
                .findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Customer with ID %d not found", orderDTO.getCustomerId())));
        order.setCustomer(customer);
        if (CollectionUtils.isNotEmpty(orderDTO.getProducts())) {
            Set<Product> products = orderDTO.getProducts().stream()
                    .map(productId -> productRepository
                            .findById(productId)
                            .orElseThrow(() -> new EntityNotFoundException(
                                    String.format("Product with ID %d not found", productId))))
                    .collect(Collectors.toSet());
            order.setProducts(products);
        } else {
            throw new IllegalArgumentException("At least one product is required for an order");
        }

        order = orderRepository.save(order);
        return orderMapper.toOrderDto(order);
    }
}
