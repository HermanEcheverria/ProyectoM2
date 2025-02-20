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

@Path("/api/servicios")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ServicioResource {

    @Inject
    ServicioService servicioService;

    @GET
    public List<Servicio> listarServicios() {
        return servicioService.listarTodos();
    }

    @POST
    @Transactional
    public Servicio agregarServicio(Servicio servicio) {
        return servicioService.agregarServicio(servicio);
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public void eliminarServicio(@PathParam("id") Long id) {
        servicioService.eliminarServicio(id);
    }
}
