package com.unis.service;

import com.unis.model.SolicitudHospital;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
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
            System.out.println(" Error al enviar solicitud a la aseguradora: " + e.getMessage());
        }
    }

    @Transactional
    public void actualizarEstado(String nombreHospital, String estado) {
        SolicitudHospital solicitud = SolicitudHospital.find("nombre", nombreHospital).firstResult();
        if (solicitud != null) {
            solicitud.estado = estado;
            solicitud.persist();
        }
    }
}
