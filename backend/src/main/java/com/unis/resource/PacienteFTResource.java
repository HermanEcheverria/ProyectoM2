package com.unis.resource;

import com.unis.model.PacienteFT;
import com.unis.service.PacienteFTService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/pacientes-ft")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteFTResource {

    @Inject
    PacienteFTService pacienteFTService;

    @GET
    public List<PacienteFT> obtenerTodosLosPacientes() {
        return pacienteFTService.getAllPacientes();
    }

    @POST
    public void registrarPaciente(PacienteFT paciente) {
        pacienteFTService.registrarPaciente(paciente);
    }
}
