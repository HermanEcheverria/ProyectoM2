package com.unis.resource;

import com.unis.model.DoctorImage;
import com.unis.service.DoctorImageService;
import com.unis.service.DoctorAccService;
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
    DoctorAccService doctorAccService;

    @GET
    @Path("/{id}")
    public Response getDoctorImage(@PathParam("id") Long idDoctor) {
        Optional<DoctorImage> imageOpt = doctorImageService.getDoctorImageByDoctorId(idDoctor);
        Optional<DoctorAcc> doctorOpt = doctorAccService.getDoctorById(idDoctor);

        if (imageOpt.isPresent() && doctorOpt.isPresent()) {
            DoctorImage image = imageOpt.get();
            DoctorAcc doctor = doctorOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("idDoctor", doctor.getIdDoctor());
            response.put("numeroColegiado", doctor.getNumeroColegiado());
            response.put("fotografia", image.getFotografia());
            response.put("fotoTitulo", image.getFotoTitulo());

            return Response.ok(response).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}/update")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response updateDoctorImage(@PathParam("id") Long idDoctor, byte[] fotografia, byte[] fotoTitulo) {
        doctorImageService.updateDoctorImage(idDoctor, fotografia, fotoTitulo);
        return Response.ok("Imagen actualizada correctamente").build();
    }

    @DELETE
    @Path("/{id}/delete")
    public Response deleteDoctorImage(@PathParam("id") Long idDoctor) {
        doctorImageService.deleteDoctorImage(idDoctor);
        return Response.ok("Imagen eliminada").build();
    }
}
