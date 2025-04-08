package com.unis.resource;

import com.unis.model.Receta;
import com.unis.model.RecetaMedicamento;
import com.unis.service.RecetaService;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

        @GET
@Path("/cita/{idCita}")
@Produces(MediaType.APPLICATION_JSON)
public Response obtenerRecetaPorIdCita(@PathParam("idCita") int idCita) {
    Receta receta = recetaService.buscarPorIdCita(idCita);
    if (receta != null) {
        return Response.ok(receta).build();
    } else {
        return Response.status(Response.Status.NOT_FOUND)
                .entity("Receta no encontrada para la cita con ID: " + idCita)
                .build();
    }
}

@PUT
@Path("/{idReceta}")  // 📌 Asegura que se recibe el ID de la receta y NO el idCita
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public Response actualizarReceta(@PathParam("idReceta") Long idReceta, Receta recetaActualizada) {
    try {
        Receta recetaEditada = recetaService.actualizarReceta(idReceta, recetaActualizada);
        return Response.ok(recetaEditada).build();
    } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error al actualizar la receta: " + e.getMessage())
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


    @GET
    @Path("/{codigoReceta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerRecetaPorCodigo(@PathParam("codigoReceta") String codigoReceta) {
        Receta receta = recetaService.buscarPorCodigo(codigoReceta);
        if (receta != null) {
            return Response.ok(receta).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Receta no encontrada con código: " + codigoReceta)
                    .build();
        }
    }



}
