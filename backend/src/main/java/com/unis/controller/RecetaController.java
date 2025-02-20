package com.unis.controller;

import java.util.List;

import com.unis.model.RecetaDTO;
import com.unis.service.RecetaService;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/recetas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecetaController {

    @Inject
    RecetaService recetaService;

    // Crear nueva receta
    @POST
    @RolesAllowed({"doctor", "administrador"})
    @Transactional
    public Response crearReceta(RecetaDTO receta) {
        try {
            recetaService.crearReceta(receta);
            return Response.status(Response.Status.CREATED)
                           .entity("{\"mensaje\":\"Receta creada exitosamente\"}")
                           .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"Error al crear receta\",\"detalle\":\"" + e.getMessage() + "\"}")
                           .type(MediaType.APPLICATION_JSON)
                           .build();
        }
    }

    // Obtener recetas por paciente
    @GET
    @Path("/paciente/{idPaciente}")
    @RolesAllowed({"paciente", "doctor", "empleado", "admin"})
    public Response obtenerRecetas(@PathParam("idPaciente") int idPaciente) {
        try {
            List<RecetaDTO> recetas = recetaService.obtenerRecetasPorPaciente(idPaciente);
            if (recetas == null || recetas.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"mensaje\":\"No se encontraron recetas para el paciente indicado.\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }
            return Response.ok(recetas).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener recetas\",\"detalle\":\"" + e.getMessage() + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }
}