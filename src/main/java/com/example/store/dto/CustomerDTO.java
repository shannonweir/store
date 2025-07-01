package com.example.store.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CustomerDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;
}
