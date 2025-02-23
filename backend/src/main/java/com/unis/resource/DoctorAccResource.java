package com.unis.resource;

import com.unis.model.DoctorAcc;
import com.unis.service.DoctorAccService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Optional;

@Path("/doctores")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoctorAccResource {
    @Inject
    DoctorAccService doctorAccService;

    @GET
    @Path("/{id}")
    public Response getDoctorById(@PathParam("id") Long id) {
        Optional<DoctorAcc> doctor = doctorAccService.getDoctorById(id);
        return doctor.isPresent() ? Response.ok(doctor.get()).build() : Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateDoctor(@PathParam("id") Long id, DoctorAcc doctor) {
        doctorAccService.updateDoctor(id, doctor);
        return Response.ok().build();
    }
}
