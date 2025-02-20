package com.unis.resource;

import com.unis.model.Empleado;
import com.unis.service.EmpleadoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/empleado")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {

    @Inject
    EmpleadoService empleadoService;

    @GET
    public List<Empleado> obtenerTodosLosEmpleados() {
        return empleadoService.getAllEmpleados();
    }

    @GET
    @Path("/{id}")
    public Response obtenerEmpleado(@PathParam("id") Long id) {
        Optional<Empleado> empleado = empleadoService.getEmpleadoById(id);
        return empleado.isPresent() ? Response.ok(empleado.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response registrarEmpleado(Empleado empleado) {
        empleadoService.registrarEmpleado(empleado);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarEmpleado(@PathParam("id") Long id, Empleado empleado) {
        boolean actualizado = empleadoService.actualizarEmpleado(id, empleado);
        return actualizado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarEmpleado(@PathParam("id") Long id) {
        boolean eliminado = empleadoService.eliminarEmpleado(id);
        return eliminado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}