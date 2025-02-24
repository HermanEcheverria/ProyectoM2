package com.unis.controller;

import java.util.List;
import com.unis.model.Receta;
import com.unis.service.RecetaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/recetas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecetaController {

    @Inject
    RecetaService recetaService;

    @GET
    public List<Receta> getRecetas() {
        return recetaService.listar();
    }

    @GET
    @Path("/{id}")
    public Response getReceta(@PathParam("id") Long id) {
        Receta receta = recetaService.obtenerPorId(id);
        if (receta == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(receta).build();
    }

    @GET
    @Path("/paciente/{idPaciente}/activas")
    public Response getRecetasActivasPorPaciente(@PathParam("idPaciente") Long idPaciente) {
    List<Receta> recetas = recetaService.obtenerRecetasPorPacienteYEstado(idPaciente, "activa");
    if (recetas.isEmpty()) {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    return Response.ok(recetas).build();
    }

    @GET
    @Path("/paciente/{idPaciente}/historial")
    public Response getHistorialRecetasPorPaciente(@PathParam("idPaciente") Long idPaciente) {
    List<Receta> recetas = recetaService.obtenerPorPaciente(idPaciente);
    if (recetas.isEmpty()) {
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    return Response.ok(recetas).build();
    }


    @POST
    @Path("/crear") 
    public Response createReceta(Receta receta) {
        Receta creada = recetaService.crear(receta);
        return Response.status(Response.Status.CREATED).entity(creada).build();
    }

    @PUT
    @Path("/editar/{id}")
    public Response updateReceta(@PathParam("id") Long id, Receta receta) {
        Receta actualizada = recetaService.actualizar(id, receta);
        if (actualizada == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(actualizada).build();
    }

    @DELETE
    @Path("/eliminar/{id}")
    public Response deleteReceta(@PathParam("id") Long id) {
        boolean eliminado = recetaService.eliminar(id);
        if (!eliminado) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}