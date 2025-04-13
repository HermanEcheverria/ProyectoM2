// CitaResource.java
package com.unis.resource;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.unis.dto.CitaDTO;
import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.service.CitaService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/citas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitaResource {

    @Inject
    CitaService citaService;

    @GET
    public List<Cita> obtenerCitas() {
        return citaService.obtenerCitas();
    }

    @GET
    @Path("/{id}")
    public Cita obtenerCita(@PathParam("id") Long id) {
        return citaService.obtenerCitaPorId(id);
    }

    @POST
    public Response agendarCita(Cita cita) {
        if (cita.getHoraInicio() == null || cita.getHoraInicio().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("⚠️ Error: La hora de inicio es obligatoria").build();
        }
        if (cita.getHoraFin() == null || cita.getHoraFin().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("⚠️ Error: La hora de fin es obligatoria").build();
        }
        if (cita.getHoraInicio().compareTo(cita.getHoraFin()) >= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("⚠️ Error: La hora de fin debe ser posterior a la hora de inicio").build();
        }

        citaService.agendarCita(cita);
        return Response.status(Response.Status.CREATED)
                .entity("✅ Cita agendada con éxito").build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarCita(@PathParam("id") Long id, Cita citaActualizada) {
        try {
            citaService.actualizarCita(id, citaActualizada);
            return Response.ok("✅ Cita actualizada con éxito").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("⚠️ Error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/cancelar")
    public Response cancelarCita(@PathParam("id") Long id) {
        try {
            citaService.cancelarCita(id);
            return Response.ok("Cita cancelada").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("⚠️ Error: " + e.getMessage()).build();
        }
    }

    @PUT
    @Path("/{id}/reasignar")
    public Response reasignarDoctor(@PathParam("id") Long id, JsonNode body) {
        try {
            Long idDoctor = body.get("idDoctor").asLong();
            Doctor nuevoDoctor = citaService.buscarDoctorPorId(idDoctor);
            if (nuevoDoctor == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("⚠️ Doctor no encontrado").build();
            }

            citaService.reasignarDoctor(id, nuevoDoctor);
            return Response.ok("Doctor reasignado con éxito").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("❌ Error en la reasignación").build();
        }
    }

    @PUT
    @Path("/{id}/procesar")
    public Response procesarCita(@PathParam("id") Long id, JsonNode body) {
        try {
            String diagnostico = body.get("diagnostico").asText(null);
            String resultados = body.get("resultados").asText(null);
            citaService.procesarCitaYEnviarResultados(id, diagnostico, resultados);
            return Response.ok("✅ Cita procesada y resultados enviados").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error al procesar la cita").build();
        }
    }

    @POST
    @Path("/externa")
    @Transactional
    public Response recibirDesdeAseguradora(CitaDTO dto) {
        try {
            citaService.recibirCitaExternaDesdeAseguradora(dto);
            return Response.status(Response.Status.CREATED).entity(" Cita recibida desde aseguradora").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Error al procesar la cita externa: " + e.getMessage()).build();
        }
    }
}