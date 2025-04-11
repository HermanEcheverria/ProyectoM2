package com.unis.resource;

import java.util.List;

import com.unis.model.SolicitudHospital;
import com.unis.service.SolicitudHospitalService;

import io.quarkus.panache.common.Sort;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hospital/solicitudes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SolicitudHospitalResource {

    @Inject
    SolicitudHospitalService servicio;

    @POST
    @Transactional
    public Response enviar(SolicitudHospital solicitud) {
        try {
            // Guardar solicitud en base de datos local
            solicitud.persist();
            System.out.println("✅ Solicitud guardada en la base de datos local.");

            // Enviar a la aseguradora (REST Client)
            servicio.enviarSolicitud(solicitud);

            // Devolver la solicitud guardada
            return Response.ok(solicitud).build();
        } catch (Exception e) {
            System.err.println("❌ Error al procesar la solicitud: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error al procesar la solicitud").build();
        }
    }

    @GET
    @Path("/historial")
    public List<SolicitudHospital> listar() {
        return SolicitudHospital.listAll(Sort.descending("id"));
    }

    //  actualizar estado de solicitud por nombre (usado por la aseguradora)
    @PATCH
    @Path("/{nombre}/estado")
    @Consumes(MediaType.TEXT_PLAIN)
    @Transactional
    public Response actualizarEstado(@PathParam("nombre") String nombre, String nuevoEstado) {
        servicio.actualizarEstado(nombre, nuevoEstado);
        return Response.ok().build();
    }
}
