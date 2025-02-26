package com.unis.resource;

import com.unis.model.RecetaMedicamento;
import com.unis.service.RecetaMedicamentoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/receta-medicamentos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecetaMedicamentoResource {

    @Inject
    RecetaMedicamentoService recetaMedicamentoService;

    @GET
    @Path("/{idReceta}")
    public List<RecetaMedicamento> listarPorReceta(@PathParam("idReceta") Long idReceta) {
        return recetaMedicamentoService.listarPorReceta(idReceta);
    }

    @POST
    public Response agregarMedicamento(RecetaMedicamento recetaMedicamento) {
        recetaMedicamentoService.agregarMedicamentoAReceta(recetaMedicamento);
        return Response.status(Response.Status.CREATED).build();
    }

    @DELETE
    @Path("/{idRecetaMedicamento}")
    public Response eliminarMedicamento(@PathParam("idRecetaMedicamento") Long idRecetaMedicamento) {
        boolean eliminado = recetaMedicamentoService.eliminar(idRecetaMedicamento);
        return eliminado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
