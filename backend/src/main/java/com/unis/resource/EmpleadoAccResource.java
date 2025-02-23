package com.unis.resource;

import com.unis.model.EmpleadoAcc;
import com.unis.service.EmpleadoAccService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Path("/empleados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoAccResource {
    @Inject
    EmpleadoAccService empleadoAccService;

    @GET
    @Path("/{id}")
    public Response getEmpleadoById(@PathParam("id") Long id) {
        Optional<EmpleadoAcc> empleado = empleadoAccService.getEmpleadoById(id);
        return empleado.isPresent() ? Response.ok(empleado.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateEmpleado(@PathParam("id") Long id, EmpleadoAcc empleado) {
        empleadoAccService.updateEmpleado(id, empleado);
        return Response.ok().build();
    }
}
