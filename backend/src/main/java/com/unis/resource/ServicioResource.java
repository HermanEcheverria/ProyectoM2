package com.unis.resource;

import java.util.List;

import com.unis.model.Servicio;
import com.unis.service.ServicioService;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

@Path("/api/servicios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServicioResource {

    @Inject
    ServicioService servicioService;

    private static final Logger LOGGER = Logger.getLogger(ServicioResource.class); // 🔹 Agregar Logger

    @GET
    public List<Servicio> listarServicios() {
        return servicioService.listarTodos();
    }

    @GET
    @Path("/{id}/subservicios")
    public List<Servicio> listarSubServicios(@PathParam("id") Long id) {
        return servicioService.listarSubServicios(id);
    }

    @POST
    @Transactional
    public Response agregarServicio(Servicio servicio) {
        try {
            if (servicio == null || servicio.nombre == null || servicio.nombre.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"El nombre del servicio es obligatorio.\"}").build();
            }

            Long parentId = servicio.getParentId(); // 🔹 Obtener `parentId` desde el body

            if (parentId != null) {
                Servicio servicioPadre = servicioService.buscarPorId(parentId);
                if (servicioPadre == null) {
                    return Response.status(Response.Status.BAD_REQUEST)
                            .entity("{\"error\": \"El servicio padre no existe.\"}").build();
                }
                servicio.setParentId(parentId);
            }

            Servicio nuevoServicio = servicioService.agregarServicio(servicio, parentId);
            return Response.status(Response.Status.CREATED).entity(nuevoServicio).build();

        } catch (Exception e) {
            LOGGER.error("Error al agregar servicio", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al agregar servicio: " + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("/{id}/subservicios")
    @Transactional
    public Response agregarSubServicio(@PathParam("id") Long parentId, Servicio subServicio) {
        try {
            if (subServicio == null || subServicio.nombre == null || subServicio.nombre.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\": \"El nombre del subservicio es obligatorio.\"}").build();
            }

            subServicio.setParentId(parentId); // 🔹 Asignar `parentId` correctamente
            Servicio nuevoSubServicio = servicioService.agregarServicio(subServicio, parentId);

            return Response.status(Response.Status.CREATED).entity(nuevoSubServicio).build();

        } catch (Exception e) {
            LOGGER.error("Error al agregar subservicio", e); // 🔹 Reemplazamos `printStackTrace()`
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al agregar subservicio: " + e.getMessage() + "\"}").build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response eliminarServicio(@PathParam("id") Long id) {
        try {
            servicioService.eliminarServicio(id);
            return Response.ok().build();
        } catch (Exception e) {
            LOGGER.error("Error al eliminar servicio", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\": \"Error al eliminar el servicio.\"}").build();
        }
    }
}
