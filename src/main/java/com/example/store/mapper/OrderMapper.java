package com.example.store.mapper;

import com.example.store.dto.CreateOrderDTO;
import com.example.store.dto.OrderDTO;
import com.example.store.entity.Order;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(source = "customer", target = "customer")
    OrderDTO toOrderDto(Order order);

    List<OrderDTO> toOrderDtos(List<Order> orders);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "products", ignore = true)
    Order toOrder(OrderDTO orderDto);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "products", ignore = true)
    Order toOrder(CreateOrderDTO orderDto);
}
