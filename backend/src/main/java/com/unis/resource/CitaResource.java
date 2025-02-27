package com.unis.resource;

import java.util.List;

import com.unis.model.Cita;
import com.unis.service.CitaService;

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

    @DELETE
    @Path("/{id}")
    public Response cancelarCita(@PathParam("id") Long id) {
        citaService.cancelarCita(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
