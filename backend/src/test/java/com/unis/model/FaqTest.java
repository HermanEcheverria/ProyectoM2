package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class FaqTest {

    @Test
    void settersYGetters_cubrenTodosLosCampos() {
        Faq faq = new Faq();

        String pregunta = "¿Cuál es el horario de atención?";
        String respuesta = "De 8:00 a 18:00 hrs.";
        String autor = "Admin";
        LocalDateTime fecha = LocalDateTime.of(2025, 8, 27, 19, 0, 0);
        String status = "PROCESO";
        String rejection = "Falta información";
        String editadoPor = "moderador1";

        // Setters
        faq.setPregunta(pregunta);
        faq.setRespuesta(respuesta);
        faq.setAutor(autor);
        faq.setFechaCreacion(fecha);
        faq.setStatus(status);
        faq.setRejectionReason(rejection);
        faq.setEditadoPor(editadoPor);

        // Getters (todos los que el reporte marcó con 0%)
        // getId() no tiene setter; solo verificamos que exista la invocación sin NPE
        assertNull(faq.getId());
        assertEquals(pregunta, faq.getPregunta());
        assertEquals(respuesta, faq.getRespuesta());
        assertEquals(autor, faq.getAutor());
        assertEquals(fecha, faq.getFechaCreacion());
        assertEquals(status, faq.getStatus());
        assertEquals(rejection, faq.getRejectionReason());
        assertEquals(editadoPor, faq.getEditadoPor());
    }

    @Test
    void onCreate_prePersist_asignaFechaCreacion() {
        Faq faq = new Faq();

        assertNull(faq.getFechaCreacion(), "Antes de onCreate, fechaCreacion debe ser null");

        // Capturamos un rango de tiempo alrededor de la llamada
        LocalDateTime antes = LocalDateTime.now();
        faq.onCreate(); // @PrePersist (es protected pero estamos en el mismo paquete)
        LocalDateTime despues = LocalDateTime.now();

        assertNotNull(faq.getFechaCreacion(), "onCreate debe asignar fechaCreacion");
        // La fecha asignada debe estar dentro del rango [antes, despues]
        assertFalse(faq.getFechaCreacion().isBefore(antes));
        assertFalse(faq.getFechaCreacion().isAfter(despues));
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull() {
        Faq faq = new Faq();

        faq.setPregunta(null);
        faq.setRespuesta(null);
        faq.setAutor(null);
        faq.setFechaCreacion(null);
        faq.setStatus(null);
        faq.setRejectionReason(null);
        faq.setEditadoPor(null);

        assertNull(faq.getPregunta());
        assertNull(faq.getRespuesta());
        assertNull(faq.getAutor());
        assertNull(faq.getFechaCreacion());
        assertNull(faq.getStatus());
        assertNull(faq.getRejectionReason());
        assertNull(faq.getEditadoPor());
    }
}
