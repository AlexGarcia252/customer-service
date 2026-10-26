package com.bank.customer.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "customers")
public class Customer extends PanacheEntity {

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false)
    public String name;

    @NotBlank(message = "El documento de identidad es obligatorio")
    @Column(name = "id_document", nullable = false, unique = true)
    public String idDocument;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    @Column(nullable = false, unique = true)
    public String email;

    @NotBlank(message = "El estado es obligatorio")
    @Column(nullable = false)
    public String status; // ACTIVE, INACTIVE
}
