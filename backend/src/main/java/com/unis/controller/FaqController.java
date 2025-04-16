package com.unis.controller;

import com.unis.model.Faq;
import com.unis.service.FaqService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/faq")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FaqController {

    @Inject
    FaqService faqService;

    // GET: Listar todas las preguntas
    @GET
    public List<Faq> listarPreguntas() {
        return faqService.listarPreguntas();
    }

    // GET: Obtener un FAQ por ID (para modo corrección)
@GET
@Path("/{id}")
public Response obtenerFaqPorId(@PathParam("id") Long id) {
    Faq faq = faqService.buscarPorId(id);
    if (faq == null) {
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    return Response.ok(faq).build();
}


    // POST: Crear nueva pregunta en estado PROCESO
    @POST
    @Path("/crear")
    @Transactional
    public Response guardarPregunta(Faq faq) {
        if (faq.getPregunta() == null || faq.getPregunta().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("La pregunta no puede estar vacía.").build();
        }
        if (faq.getEditadoPor() == null) {
            throw new IllegalArgumentException("Editor email es requerido");
        } 
        faq.setStatus("PROCESO");
        faqService.guardarPregunta(faq);
        return Response.ok(faq).status(Response.Status.CREATED).build();
    }

    // PUT: Editar pregunta (mantiene el status actual)
    @PUT
    @Path("/editar/{id}")
    @Transactional
    public Response editarPregunta(@PathParam("id") Long id, Faq faqActualizada) {
        Faq faq = faqService.buscarPorId(id);
        if (faq == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Pregunta no encontrada").build();
        }
    
        if (faqActualizada.getEditadoPor() == null || faqActualizada.getEditadoPor().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("El campo 'editadoPor' es obligatorio.").build();
        }
    
        if (faqActualizada.getPregunta() != null) faq.setPregunta(faqActualizada.getPregunta());
        if (faqActualizada.getRespuesta() != null) faq.setRespuesta(faqActualizada.getRespuesta());
        if (faqActualizada.getAutor() != null) faq.setAutor(faqActualizada.getAutor());
        faq.setEditadoPor(faqActualizada.getEditadoPor()); // ya validado
        if (faqActualizada.getStatus() != null) faq.setStatus(faqActualizada.getStatus());
        if (faqActualizada.getRejectionReason() != null) faq.setRejectionReason(faqActualizada.getRejectionReason());
    
        faqService.actualizarFaq(faq);
        return Response.ok(faq).build();
    }
    

    // DELETE: Eliminar pregunta
    @DELETE
    @Path("/eliminar/{id}")
    @Transactional
    public Response eliminarPregunta(@PathParam("id") Long id) {
        boolean eliminado = faqService.eliminarFaq(id);
        if (!eliminado) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Pregunta no encontrada").build();
        }
        return Response.ok().build();
    }

    // GET: Listar preguntas con estado PROCESO
    @GET
    @Path("/pendientes")
    public List<Faq> listarPendientes() {
        return faqService.listarPorEstado("PROCESO");
    }

    // PUT: Aprobar contenido
    @PUT
    @Path("/aprobar/{id}")
    @Transactional
    public Response aprobarPregunta(@PathParam("id") Long id) {
        Faq faq = faqService.buscarPorId(id);
        if (faq == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        faq.setStatus("PUBLICADO");
        faq.setRejectionReason(null);
        faqService.actualizarFaq(faq);
        return Response.ok(faq).build();
    }

    // PUT: Rechazar contenido
    @PUT
    @Path("/rechazar/{id}")
    @Transactional
    public Response rechazarPregunta(@PathParam("id") Long id, @QueryParam("motivo") String motivo) {
        Faq faq = faqService.buscarPorId(id);
        if (faq == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        faq.setStatus("RECHAZADO");
        faq.setRejectionReason(motivo);
        faqService.actualizarFaq(faq);
        return Response.ok(faq).build();
    }
}
