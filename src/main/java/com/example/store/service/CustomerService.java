package com.example.store.service;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;
import com.example.store.mapper.CustomerMapper;
import com.example.store.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Cacheable(value = "customers", key = "'all'")
    public List<CustomerDTO> getAllCustomers() {
        return customerMapper.toCustomerDtos(customerRepository.findAll());
    }

    public List<CustomerDTO> getCustomersByNameContaining(String name) {
        return customerMapper.toCustomerDtos(customerRepository.findByNameContainingIgnoreCase(name));
    }

    @Transactional
    @CacheEvict(value = "customers", allEntries = true)
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = customerRepository.save(customerMapper.toCustomer(customerDTO));
        return customerMapper.toCustomerDto(customer);
    }
}
