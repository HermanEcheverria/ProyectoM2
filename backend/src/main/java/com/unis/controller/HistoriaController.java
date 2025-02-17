package com.unis.controller;

import java.util.List;

import com.unis.model.Historia;
import com.unis.service.HistoriaService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/historias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HistoriaController {

    @Inject
    HistoriaService historiaService;

    /**
     * Obtiene la lista de todas las historias.
     */
    @GET
    public List<Historia> getHistorias() {
        return historiaService.listar();
    }

    /**
     * Obtiene una historia por su ID.
     *
     * @param id el ID de la historia
     * @return la historia encontrada o 404 si no existe
     */
    @GET
    @Path("/{id}")
    public Response getHistoria(@PathParam("id") Long id) {
        Historia historia = historiaService.obtenerPorId(id);
        if (historia == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(historia).build();
    }

    /**
     * Crea una nueva historia.
     *
     * @param historia la historia a crear
     * @return la historia creada con el status 201
     */
    @POST
    public Response createHistoria(Historia historia) {
        Historia creado = historiaService.crear(historia);
        return Response.status(Response.Status.CREATED).entity(creado).build();
    }

    /**
     * Actualiza una historia existente.
     *
     * @param id la ID de la historia a actualizar
     * @param historia la historia con los nuevos datos
     * @return la historia actualizada o 404 si no se encuentra
     */
    @PUT
    @Path("/{id}")
    public Response updateHistoria(@PathParam("id") Long id, Historia historia) {
        Historia actualizado = historiaService.actualizar(id, historia);
        if (actualizado == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(actualizado).build();
    }

    /**
     * Elimina una historia por su ID.
     *
     * @param id el ID de la historia a eliminar
     * @return 204 si se elimina o 404 si no se encuentra
     */
    @DELETE
    @Path("/{id}")
    public Response deleteHistoria(@PathParam("id") Long id) {
        boolean eliminado = historiaService.eliminar(id);
        if (!eliminado) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }
}
