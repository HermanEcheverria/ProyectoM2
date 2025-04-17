package com.unis.resource;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import com.unis.model.UserAcc;
import com.unis.service.UserAccService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;


@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAccResource {

    @Inject
    UserAccService userAccService;

    @GET
    @Path("/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        Optional<UserAcc> user = userAccService.getUserById(id);
        return user.map(Response::ok).orElseGet(() -> Response.status(Response.Status.NOT_FOUND)).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateUser(@PathParam("id") Long id, UserAcc updatedUser) {
        try {
            userAccService.updateUser(id, updatedUser);
            return Response.ok().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/cambiar-rol")
    public Response changeUserRole(@PathParam("id") Long id, @QueryParam("nuevoRol") int nuevoRol) {
        try {
            userAccService.changeUserRole(id, nuevoRol);
            return Response.ok().entity("Rol cambiado correctamente").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
     @GET
    @Path("/me")
    public Response getCurrentUser(@Context SecurityContext sec) {
        if (sec.getUserPrincipal() == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        String nombreUsuario = sec.getUserPrincipal().getName();
        Map<String, String> result = Collections.singletonMap("nombreUsuario", nombreUsuario);
        return Response.ok(result).build();
    }
}
