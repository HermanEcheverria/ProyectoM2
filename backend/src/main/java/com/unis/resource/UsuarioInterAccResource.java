package com.unis.resource;

import com.unis.model.UsuarioInterAcc;
import com.unis.service.UsuarioInterAccService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/usuariosinter")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioInterAccResource {

    @Inject
    UsuarioInterAccService usuarioInterAccService;

    @GET
    public List<UsuarioInterAcc> obtenerTodos() {
        return usuarioInterAccService.getAllUsuariosInterAcc();
    }

    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        Optional<UsuarioInterAcc> usuario = usuarioInterAccService.getUsuarioInterAccById(id);
        return usuario.isPresent() ? Response.ok(usuario.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarUsuario(@PathParam("id") Long id, UsuarioInterAcc usuarioInterAcc) {
        usuarioInterAccService.actualizarUsuarioInterAcc(id, usuarioInterAcc);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarUsuario(@PathParam("id") Long id) {
        usuarioInterAccService.eliminarUsuarioInterAcc(id);
        return Response.ok().build();
    }
}
