package com.unis.controller;

import com.unis.model.Historia;
import com.unis.service.HistoriaService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("/historias")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class HistoriaController {

    @Inject
    HistoriaService historiaService;

    @GET
    public List<Historia> getHistorias() {
        return historiaService.listar();
    }

    @GET
    @Path("/publicadas")
    public List<Historia> getHistoriasPublicadas() {
        return historiaService.listarPorEstado("PUBLICADO");
    }

    @GET
    @Path("/{id}")
    public Response getHistoria(@PathParam("id") Long id) {
        Historia historia = historiaService.obtenerPorId(id);
        if (historia == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(historia).build();
    }

    @POST
    @Transactional
    public Response createHistoria(Historia historia) {
        if (historia.getEditorEmail() == null || historia.getEditorEmail().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El correo del editor (editorEmail) es requerido.").build();
        }

        historia.setStatus("PROCESO");
        Historia creado = historiaService.crear(historia);
        return Response.status(Response.Status.CREATED).entity(creado).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateHistoria(@PathParam("id") Long id, Historia historiaActualizada) {
        Historia historia = historiaService.obtenerPorId(id);
        if (historia == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (historiaActualizada.getEditorEmail() == null || historiaActualizada.getEditorEmail().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("El correo del editor (editorEmail) es requerido.").build();
        }

        if (historiaActualizada.getNombreEntidad() != null)
            historia.setNombreEntidad(historiaActualizada.getNombreEntidad());
        if (historiaActualizada.getHistoria() != null)
            historia.setHistoria(historiaActualizada.getHistoria());
        if (historiaActualizada.getMeritos() != null)
            historia.setMeritos(historiaActualizada.getMeritos());
        if (historiaActualizada.getLineaDelTiempo() != null)
            historia.setLineaDelTiempo(historiaActualizada.getLineaDelTiempo());
        if (historiaActualizada.getStatus() != null)
            historia.setStatus(historiaActualizada.getStatus());
        if (historiaActualizada.getRejectionReason() != null)
            historia.setRejectionReason(historiaActualizada.getRejectionReason());

        // ✅ Muy importante: asignar siempre el editorEmail actualizado
        historia.setEditorEmail(historiaActualizada.getEditorEmail());

        historiaService.actualizar(id, historia);
        return Response.ok(historia).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteHistoria(@PathParam("id") Long id) {
        boolean eliminado = historiaService.eliminar(id);
        if (!eliminado) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/pendientes")
    public List<Historia> getPendientesModeracion() {
        return historiaService.listarPorEstado("PROCESO");
    }

    @PUT
    @Path("/aprobar/{id}")
    @Transactional
    public Response aprobarHistoria(@PathParam("id") Long id) {
        Historia historia = historiaService.obtenerPorId(id);
        if (historia == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        historia.setStatus("PUBLICADO");
        historia.setRejectionReason(null);
        return Response.ok(historiaService.actualizar(id, historia)).build();
    }

    @PUT
    @Path("/rechazar/{id}")
    @Transactional
    public Response rechazarHistoria(@PathParam("id") Long id, @QueryParam("motivo") String motivo) {
        Historia historia = historiaService.obtenerPorId(id);
        if (historia == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        historia.setStatus("RECHAZADO");
        historia.setRejectionReason(motivo);
        return Response.ok(historiaService.actualizar(id, historia)).build();
    }
}
