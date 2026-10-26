package com.bank.customer.resource;

import com.bank.customer.dto.CustomerResponseDTO;
import com.bank.customer.entity.Customer;
import com.bank.customer.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerResource {

    // RF1: Consulta de Clientes por ID
    @GET
    @Path("/{id}")
    public CustomerResponseDTO getCustomerById(@PathParam("id") Long id) {
        Customer customer = Customer.findById(id);
        if (customer == null) {
            throw new ResourceNotFoundException("Cliente con ID " + id + " no encontrado.");
        }
        return CustomerResponseDTO.fromEntity(customer);
    }

    // Endpoint auxiliar para listar todos
    @GET
    public List<CustomerResponseDTO> getAllCustomers() {
        return Customer.<Customer>listAll().stream()
                .map(CustomerResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // Endpoint auxiliar para crear clientes fácilmente
    @POST
    @Transactional
    public Response createCustomer(@Valid Customer customer) {
        // En un caso real validaríamos que el email/documento no existan ya
        customer.persist();
        return Response.status(Response.Status.CREATED)
                .entity(CustomerResponseDTO.fromEntity(customer))
                .build();
    }
}