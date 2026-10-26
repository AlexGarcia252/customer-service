package com.bank.customer.dto;

import com.bank.customer.entity.Customer;

public record CustomerResponseDTO(
        Long id,
        String name,
        String idDocument,
        String email,
        String status
) {
    public static CustomerResponseDTO fromEntity(Customer customer) {
        return new CustomerResponseDTO(
                customer.id,
                customer.name,
                customer.idDocument,
                customer.email,
                customer.status
        );
    }
}
