package com.unis.controller;

import com.unis.model.Usuario;
import com.unis.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioController {


    @Inject
    UsuarioService usuarioService;

    @GET
    public List<Usuario> listarUsuarios() {
        return usuarioService.listarUsuarios();
    }

    @POST
    @Path("/registro")
    @Transactional
    public Response registrarUsuario(Usuario usuario) {
        usuarioService.registrarUsuario(usuario);
        return Response.ok(usuario).status(201).build();
    }

    @POST
    @Path("/login")
    public Response login(Usuario usuario) {
        Usuario usuarioEncontrado = usuarioService.obtenerUsuarioPorCorreo(usuario.getCorreo());
        if (usuarioEncontrado != null && usuarioEncontrado.getContrasena().equals(usuario.getContrasena())) {
            return Response.ok(usuarioEncontrado).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Credenciales incorrectas").build();
    }
}
