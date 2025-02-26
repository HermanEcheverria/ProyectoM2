package com.unis.resource;

import com.unis.model.Receta;
import com.unis.model.RecetaMedicamento;
import com.unis.service.RecetaService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/recetas")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecetaResource {

    @Inject
    RecetaService recetaService;

    @POST
    public Response crearReceta(Receta receta) {
        try {
            Receta nuevaReceta = recetaService.crearReceta(receta);
            return Response.ok(nuevaReceta).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al crear la receta: " + e.getMessage())
                    .build();
        }
    }

    @POST
    @Path("/medicamentos")
    public Response agregarMedicamento(RecetaMedicamento recetaMedicamento) {
        try {
            RecetaMedicamento nuevoMed = recetaService.agregarMedicamento(recetaMedicamento);
            return Response.ok(nuevoMed).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al agregar medicamento: " + e.getMessage())
                    .build();
        }
    }
}
