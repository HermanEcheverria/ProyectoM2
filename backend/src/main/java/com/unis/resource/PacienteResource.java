package com.unis.resource;

import java.util.List;

import com.unis.model.Paciente;
import com.unis.repository.PacienteRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@ApplicationScoped
@Path("/paciente")  // <---- ⚠️ Verifica que esté en singular
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PacienteResource {

    @Inject
    PacienteRepository pacienteRepository;

    @GET
    public List<Paciente> listarPacientes() {
        return pacienteRepository.listAll();
    }
}
