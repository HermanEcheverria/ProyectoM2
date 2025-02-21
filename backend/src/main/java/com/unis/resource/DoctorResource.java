package com.unis.resource;

import com.unis.model.Doctor;
import com.unis.service.DoctorService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/doctor")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoctorResource {

    @Inject
    DoctorService doctorService;

    @GET
    public List<Doctor> obtenerTodosLosDoctores() {
        return doctorService.getAllDoctores();
    }

    @GET
    @Path("/{id}")
    public Response obtenerDoctor(@PathParam("id") Long id) {
        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        return doctor.isPresent() ? Response.ok(doctor.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @POST
    public Response registrarDoctor(Doctor doctor) {
        doctorService.registrarDoctor(doctor);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    public Response actualizarDoctor(@PathParam("id") Long id, Doctor doctor) {
        boolean actualizado = doctorService.actualizarDoctor(id, doctor);
        return actualizado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @DELETE
    @Path("/{id}")
    public Response eliminarDoctor(@PathParam("id") Long id) {
        boolean eliminado = doctorService.eliminarDoctor(id);
        return eliminado ? Response.ok().build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
