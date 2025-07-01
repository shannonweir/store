package com.example.store.mapper;

import com.example.store.dto.CustomerDTO;
import com.example.store.entity.Customer;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    CustomerDTO toCustomerDto(Customer customer);

    Customer toCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> toCustomerDtos(List<Customer> customers);
}
