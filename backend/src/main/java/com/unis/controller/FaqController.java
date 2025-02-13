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

    //Obtener todas las preguntas
    @GET
    public List<Faq> listarPreguntas() {
        return faqService.listarPreguntas();
    }

    //Crear una nueva pregunta
    @POST
    @Path("/crear")
    @Transactional
    public Response guardarPregunta(Faq faq) {
        if (faq.getPregunta() == null || faq.getPregunta().trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("La pregunta no puede estar vacía.").build();
        }
        faqService.guardarPregunta(faq);
        return Response.ok(faq).status(Response.Status.CREATED).build();
    }

    //Responder una pregunta
    @PUT
    @Path("/responder/{id}")
    @Transactional
    public Response responderPregunta(@PathParam("id") Long id, String respuesta) {
        Faq faq = faqService.buscarPorId(id);
        if (faq == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Pregunta no encontrada").build();
        }

        faq.setRespuesta(respuesta);
        faqService.actualizarFaq(faq);
        return Response.ok(faq).build();
    }

    //Editar una pregunta por el administrador
    @PUT
    @Path("/editar/{id}")
    @Transactional
    public Response editarPregunta(@PathParam("id") Long id, Faq faqActualizada) {
        Faq faq = faqService.buscarPorId(id);
        if (faq == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Pregunta no encontrada").build();
        }

        if (faqActualizada.getPregunta() != null) {
            faq.setPregunta(faqActualizada.getPregunta());
        }
        if (faqActualizada.getRespuesta() != null) {
            faq.setRespuesta(faqActualizada.getRespuesta());
        }
        if (faqActualizada.getAutor() != null) {
            faq.setAutor(faqActualizada.getAutor());
        }
        if (faqActualizada.getEditadoPor() != null) {
            faq.setEditadoPor(faqActualizada.getEditadoPor());
        }

        faqService.actualizarFaq(faq);
        return Response.ok(faq).build();
    }

    //Eliminar una pregunta
    @DELETE
    @Path("/eliminar/{id}")
    @Transactional
    public Response eliminarPregunta(@PathParam("id") Long id) {
        boolean eliminado = faqService.eliminarFaq(id);
        if (!eliminado) {
            return Response.status(Response.Status.NOT_FOUND).entity("Pregunta no encontrada").build();
        }
        return Response.ok().build();
    }
}
