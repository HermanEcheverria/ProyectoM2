package com.unis.resource;

import com.unis.model.Paciente;
import com.unis.service.PacienteService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/paciente")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    @Inject
    PacienteService pacienteService;

    @GET
    public List<Paciente> obtenerTodosLosPacientes() {
        return pacienteService.getAllPacientes();
    }

    @GET
    @Path("/{id}")
    public Response obtenerPaciente(@PathParam("id") Long id) {
        Optional<Paciente> paciente = pacienteService.getPacienteById(id);
        return paciente.isPresent() ? Response.ok(paciente.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response registrarPaciente(Paciente paciente) {
        pacienteService.registrarPaciente(paciente);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarPaciente(@PathParam("id") Long id, Paciente paciente) {
        boolean actualizado = pacienteService.actualizarPaciente(id, paciente);
        return actualizado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarPaciente(@PathParam("id") Long id) {
        boolean eliminado = pacienteService.eliminarPaciente(id);
        return eliminado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
