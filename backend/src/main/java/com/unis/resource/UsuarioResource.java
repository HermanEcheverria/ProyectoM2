package com.unis.resource;

import com.unis.model.Usuario;
import com.unis.service.UsuarioService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/api/usuarios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UsuarioResource {

    @Inject
    UsuarioService usuarioService;

    // 🔹 Endpoint de registro de usuario
    @POST
    @Path("/registro")
    @Transactional
    public Response registrarUsuario(Usuario usuario) {
        usuarioService.registrarUsuario(usuario);

        Map<String, String> respuesta = new HashMap<>();
        respuesta.put("mensaje", "Usuario registrado con éxito");

        return Response.status(Response.Status.CREATED).entity(respuesta).build();
    }
}
