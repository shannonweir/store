package com.example.store.controller;

import com.example.store.dto.CustomerDTO;
import com.example.store.service.CustomerService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/search")
    public List<CustomerDTO> getCustomersByNameContaining(@RequestParam final String name) {
        return customerService.getCustomersByNameContaining(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@RequestBody final CustomerDTO customerDTO) {
        return customerService.createCustomer(customerDTO);
    }


}
