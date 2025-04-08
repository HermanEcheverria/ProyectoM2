package com.unis.resource;

import com.unis.model.SolicitudHospital;
import com.unis.service.SolicitudHospitalService;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import io.quarkus.panache.common.Sort;

@Path("/hospital/solicitudes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SolicitudHospitalResource {

    @Inject
    SolicitudHospitalService servicio;

    @POST
    @Transactional
    public Response enviar(SolicitudHospital solicitud) {
        //  Guardar solicitud en base de datos local
        solicitud.persist();

        //  Enviar a la aseguradora (REST Client)
        servicio.enviarSolicitud(solicitud);
        //  Devolver la solicitud guardada
        return Response.ok(solicitud).build();
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
