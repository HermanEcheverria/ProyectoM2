package com.unis.resource;

import com.unis.service.DoctorImageService;
import com.unis.service.UserAccService;
import com.unis.model.UserAcc;
import com.unis.repository.DoctorAccRepository;
import com.unis.model.DoctorAcc;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Path("/api/doctor-images")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DoctorImageResource {

    @Inject
    DoctorImageService doctorImageService;

    @Inject
    UserAccService userAccService;

    @Inject
    DoctorAccRepository doctorAccRepository; // Agregado para obtener la información del doctor

    @GET
    @Path("/{id}")
    public Response getDoctorData(@PathParam("id") Long id) {
        return doctorImageService.getDoctorImageById(id).map(doctorImage -> {
            Map<String, Object> response = new HashMap<>();
            response.put("idDoctor", doctorImage.getIdDoctor());
            response.put("fotografia", doctorImage.getFotografia());
            response.put("fotoTitulo", doctorImage.getFotoTitulo());

            // Obtener la información del doctor desde la tabla DOCTOR_ACC
            Optional<DoctorAcc> doctorAccOpt = doctorAccRepository.find("idDoctor", id).firstResultOptional();
            if (doctorAccOpt.isPresent()) {
                DoctorAcc doctorAcc = doctorAccOpt.get();
                response.put("apellido", doctorAcc.getApellido());

                // Obtener el usuario basado en el ID del doctor
                Optional<UserAcc> userOpt = userAccService.getUserById(doctorAcc.getIdUsuario());
                if (userOpt.isPresent()) {
                    UserAcc user = userOpt.get();
                    response.put("nombre", user.getNombreUsuario());
                    response.put("correo", user.getCorreo());
                } else {
                    response.put("nombre", "Desconocido");
                    response.put("correo", "No disponible");
                }
            } else {
                response.put("apellido", "No disponible");
                response.put("nombre", "Desconocido");
                response.put("correo", "No disponible");
            }

            return Response.ok(response).build();
        }).orElse(Response.status(Response.Status.NOT_FOUND).build());
    }
}
