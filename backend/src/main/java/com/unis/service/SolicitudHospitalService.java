package com.unis.service;

import com.unis.model.SolicitudHospital;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class SolicitudHospitalService {

    @Inject
    @RestClient
    SolicitudHospitalAPI aseguradoraClient;

    public void enviarSolicitud(SolicitudHospital solicitud) {
        try {
            aseguradoraClient.enviarSolicitud(solicitud);
        } catch (Exception e) {
            System.out.println("❌ Error al enviar solicitud a la aseguradora: " + e.getMessage());
        }
    }
}
