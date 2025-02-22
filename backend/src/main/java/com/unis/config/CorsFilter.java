package com.unis.config;

import java.io.IOException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

@Provider  //  Esto indica que es un filtro de JAX-RS
@ApplicationScoped  //  Asegura que sea un Singleton en Quarkus
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        responseContext.getHeaders().add("Access-Control-Allow-Origin", "*");
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "X-Requested-With, Authorization, Content-Type, Accept");

        //  Manejo especial para solicitudes OPTIONS (preflight)
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            responseContext.setStatus(200);
        }
    }
}
