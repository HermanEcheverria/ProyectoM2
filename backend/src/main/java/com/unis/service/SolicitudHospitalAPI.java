package com.unis.service;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.unis.model.SolicitudHospital;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/solicitudes/hospital") // Cambiar al path correcto del sistema de la aseguradora
@RegisterRestClient(configKey = "solicitud-hospital-api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface SolicitudHospitalAPI {

    @POST
    SolicitudHospital enviarSolicitud(SolicitudHospital hospital);
}
