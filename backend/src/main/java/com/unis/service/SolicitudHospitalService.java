package com.unis.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.unis.model.SolicitudHospital;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Servicio para gestionar las solicitudes de hospitales hacia aseguradoras y MongoDB.
 */
@ApplicationScoped
public class SolicitudHospitalService {

    @Inject
    @RestClient
    SolicitudHospitalAPI aseguradoraClient;

    /**
     * Envía una solicitud de hospital a la aseguradora y MongoDB.
     *
     * @param solicitud Los datos de la solicitud a enviar.
     */
    public void enviarSolicitud(SolicitudHospital solicitud) {
        try {
            // Validar datos antes de enviar
            if (solicitud.nombre == null || solicitud.nombre.isEmpty() ||
                solicitud.direccion == null || solicitud.direccion.isEmpty() ||
                solicitud.telefono == null || solicitud.telefono.isEmpty() ||
                solicitud.aseguradora == null || solicitud.aseguradora.isEmpty()) {
                System.err.println("❌ Datos incompletos. No se puede enviar la solicitud: " + solicitud);
                return;
            }

            System.out.println("📤 Enviando solicitud a la aseguradora: " + solicitud);
            System.out.println("📋 Datos enviados: " +
                "Nombre: " + solicitud.nombre + ", " +
                "Dirección: " + solicitud.direccion + ", " +
                "Teléfono: " + solicitud.telefono + ", " +
                "Aseguradora: " + solicitud.aseguradora + ", " +
                "Estado: " + solicitud.estado);

            // Enviar solicitud a la aseguradora
            aseguradoraClient.enviarSolicitud(solicitud);
            System.out.println("✅ Solicitud enviada correctamente a la aseguradora.");

            // Enviar la solicitud a MongoDB
            enviarSolicitudAMongo(solicitud);
        } catch (Exception e) {
            System.err.println("❌ Error al enviar solicitud a la aseguradora o MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Envía una solicitud de hospital a MongoDB.
     *
     * @param solicitud Los datos de la solicitud a enviar.
     */
    private void enviarSolicitudAMongo(SolicitudHospital solicitud) {
        try {
            URL url = new URL("http://localhost:5000/api/solicitudes/hospital");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            String input = String.format(
                "{\"nombre\":\"%s\",\"direccion\":\"%s\",\"telefono\":\"%s\",\"aseguradora\":\"%s\",\"estado\":\"%s\",\"origen\":\"%s\"}",
                solicitud.nombre, solicitud.direccion, solicitud.telefono, solicitud.aseguradora, solicitud.estado, solicitud.origen
            );

            System.out.println("📤 Enviando solicitud a MongoDB con datos: " + input);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(input.getBytes());
                os.flush();
            }

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.err.println("❌ Error al enviar solicitud a MongoDB: " + conn.getResponseMessage());
            } else {
                System.out.println("✅ Solicitud enviada correctamente a MongoDB.");
            }

            conn.disconnect();
        } catch (Exception e) {
            System.err.println("❌ Error al enviar solicitud a MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza el estado de una solicitud de hospital.
     *
     * @param nombreHospital El nombre del hospital.
     * @param estado         El nuevo estado de la solicitud.
     */
    @Transactional
    public void actualizarEstado(String nombreHospital, String estado) {
        SolicitudHospital solicitud = SolicitudHospital.find("nombre", nombreHospital).firstResult();
        if (solicitud != null) {
            solicitud.estado = estado;
            solicitud.persist();
        }
    }
}