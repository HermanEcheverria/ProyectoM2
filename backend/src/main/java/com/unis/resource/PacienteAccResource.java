package com.unis.resource;

import com.unis.model.PacienteAcc;
import com.unis.service.PacienteAccService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Path("/pacientes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteAccResource {
    @Inject
    PacienteAccService pacienteAccService;

    @GET
    @Path("/{id}")
    public Response getPacienteById(@PathParam("id") Long id) {
        Optional<PacienteAcc> paciente = pacienteAccService.getPacienteById(id);
        return paciente.isPresent() ? Response.ok(paciente.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response updatePaciente(@PathParam("id") Long id, PacienteAcc paciente) {
        pacienteAccService.updatePaciente(id, paciente);
        return Response.ok().build();
    }
}
