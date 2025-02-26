package com.unis.resource;

import com.unis.model.Medicamento;
import com.unis.service.MedicamentoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("/medicamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicamentoResource {

    @Inject
    MedicamentoService medicamentoService;

    @GET
    public List<Medicamento> listarTodos() {
        return medicamentoService.listarTodos();
    }

    @GET
    @Path("/{id}")
    public Medicamento obtenerPorId(@PathParam("id") Long id) {
        return medicamentoService.obtenerPorId(id);
    }
}
