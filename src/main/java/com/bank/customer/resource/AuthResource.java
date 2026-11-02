package com.bank.customer.resource;

import io.smallrye.jwt.build.Jwt;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import java.util.Set;

@Path("/auth")
public class AuthResource {
    @GET
    @Path("/login")
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken(@QueryParam("role") String role) {
        String userRole = (role != null) ? role : "ROLE_USER";
        return Jwt.issuer("https://bank.com")
                .upn("test-user@bank.com")
                .groups(Set.of(userRole))
                .claim("customerId", 1L)
                .sign(); // Va a firmar automáticamente con la private key que encontraste
    }
}