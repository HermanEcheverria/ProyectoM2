package com.unis.resource;

import com.unis.model.UsuarioInter;
import com.unis.service.UsuarioInterService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/usuariointer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioInterResource {

    @Inject
    UsuarioInterService usuarioInterService;

    @GET
    public List<UsuarioInter> obtenerTodos() {
        return usuarioInterService.getAllUsuarios();
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        Optional<UsuarioInter> usuario = usuarioInterService.getUsuarioById(id);
        return usuario.isPresent() ? Response.ok(usuario.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response crearUsuario(UsuarioInter usuario) {
        usuarioInterService.registrarUsuario(usuario);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarUsuario(@PathParam("id") Long id, UsuarioInter usuario) {
        usuarioInterService.actualizarUsuario(id, usuario);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarUsuario(@PathParam("id") Long id) {
        usuarioInterService.eliminarUsuario(id);
        return Response.ok().build();
    }
}
