package com.unis.controller;

import com.unis.model.Medicamento;
import com.unis.service.MedicamentoService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/medicamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MedicamentoController {

    @Inject
    MedicamentoService medicamentoService;

    // Obtener todos los medicamentos
    @GET
    public Response obtenerTodos() {
        List<Medicamento> medicamentos = medicamentoService.listarMedicamentos();
        if (medicamentos.isEmpty()) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        return Response.ok(medicamentos).build();
    }

    // Obtener medicamento por ID
    @GET
    @Path("/{id}")
    public Response obtenerPorId(@PathParam("id") Long id) {
        Medicamento medicamento = medicamentoService.obtenerMedicamentoPorId(id);
        if (medicamento == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"Medicamento no encontrado\"}")
                           .build();
        }
        return Response.ok(medicamento).build();
    }

    // Crear medicamento (eliminando @Path("/crear"))
    @POST
    @Path("/crear")
    @Transactional
    public Response crear(Medicamento medicamento) {
        if (medicamento.getNombre() == null || medicamento.getPrincipioActivo() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("{\"error\": \"El nombre y el principio activo son obligatorios.\"}")
                           .build();
        }
        medicamentoService.crearMedicamento(medicamento);
        return Response.status(Response.Status.CREATED).entity(medicamento).build();
    }

    // Actualizar medicamento
    @PUT
    @Path("/editar/{id}")
    @Transactional
    public Response actualizar(@PathParam("id") Long id, Medicamento medicamentoActualizado) {
        Medicamento medicamentoExistente = medicamentoService.obtenerMedicamentoPorId(id);
        if (medicamentoExistente == null) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"Medicamento no encontrado\"}")
                           .build();
        }

        if (medicamentoActualizado.getNombre() != null) {
            medicamentoExistente.setNombre(medicamentoActualizado.getNombre());
        }
        if (medicamentoActualizado.getDosis() != null) {
            medicamentoExistente.setDosis(medicamentoActualizado.getDosis());
        }

        medicamentoService.actualizarMedicamento(medicamentoExistente);
        return Response.ok(medicamentoExistente).build();
    }

    // Eliminar medicamento
    @DELETE
    @Path("/eliminar/{id}")
    @Transactional
    public Response eliminar(@PathParam("id") Long id) {
        boolean eliminado = medicamentoService.eliminarMedicamento(id);
        if (!eliminado) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("{\"error\": \"Medicamento no encontrado\"}")
                           .build();
        }
        return Response.ok().build();
    }
}