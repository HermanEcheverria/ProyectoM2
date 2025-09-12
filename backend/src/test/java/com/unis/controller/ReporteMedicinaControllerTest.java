package com.unis.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.unis.dto.MedicinasReporteDTO;
import com.unis.service.ReporteMedicinaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.WebApplicationException;

public class ReporteMedicinaControllerTest {

    private ReporteMedicinaController controller;
    private ReporteMedicinaService service;

    @BeforeEach
    void setUp() {
        controller = new ReporteMedicinaController();
        service = Mockito.mock(ReporteMedicinaService.class);
        // El campo 'service' no es private, así que podemos asignarlo directamente.
        controller.service = service;
    }

    @Test
    void obtener_ok_devuelveLista() throws Exception {
        String inicio = "2024-01-01";
        String fin = "2024-01-31";
        int limite = 5;

        // DTO usando tu constructor (int, String, int)
        MedicinasReporteDTO dto = new MedicinasReporteDTO(1, "Paracetamol", 120);
        List<MedicinasReporteDTO> esperado = Arrays.asList(dto);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        Date fechaInicio = sdf.parse(inicio);
        Date fechaFin = sdf.parse(fin);

        when(service.obtenerReporte(fechaInicio, fechaFin, limite)).thenReturn(esperado);

        List<MedicinasReporteDTO> result = controller.obtener(inicio, fin, limite);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).popularidad);
        assertEquals("Paracetamol", result.get(0).principioActivo);
        assertEquals(120, result.get(0).totalRecetas);

        verify(service, times(1)).obtenerReporte(fechaInicio, fechaFin, limite);
    }

    @Test
    void obtener_formatoInvalido_lanza400() {
        String inicio = "2024/01/01"; // formato inválido
        String fin = "2024-01-31";
        int limite = 5;

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> controller.obtener(inicio, fin, limite));

        assertEquals(400, ex.getResponse().getStatus());
        verify(service, never()).obtenerReporte(any(), any(), anyInt());
    }

    @Test
    void obtener_sinInicio_lanza400() {
        String inicio = null; // falta inicio
        String fin = "2024-01-31";
        int limite = 5;

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> controller.obtener(inicio, fin, limite));

        assertEquals(400, ex.getResponse().getStatus());
        verify(service, never()).obtenerReporte(any(), any(), anyInt());
    }

    @Test
    void obtener_sinFin_lanza400() {
        String inicio = "2024-01-01";
        String fin = null; // falta fin
        int limite = 5;

        WebApplicationException ex = assertThrows(WebApplicationException.class,
                () -> controller.obtener(inicio, fin, limite));

        assertEquals(400, ex.getResponse().getStatus());
        verify(service, never()).obtenerReporte(any(), any(), anyInt());
    }
}
