package com.unis.resource;
import java.util.List;

import com.unis.model.Medicamento;
import com.unis.service.MedicamentoService;

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
    public Response obtenerPorId(@PathParam("id") Long id) {
        Medicamento med = medicamentoService.obtenerPorId(id);
        if (med == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(med).build();
    }

    @POST
    public Response crearMedicamento(Medicamento medicamento) {
        Medicamento nuevoMed = medicamentoService.crearMedicamento(medicamento);
        return Response.status(Response.Status.CREATED).entity(nuevoMed).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarMedicamento(@PathParam("id") Long id, Medicamento medicamento) {
        Medicamento actualizado = medicamentoService.actualizarMedicamento(id, medicamento);
        if (actualizado == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(actualizado).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarMedicamento(@PathParam("id") Long id) {
        boolean eliminado = medicamentoService.eliminarMedicamento(id);
        if (eliminado) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
