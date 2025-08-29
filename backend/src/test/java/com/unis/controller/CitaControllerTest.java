package com.unis.controller;

import com.unis.model.Cita;
import com.unis.service.CitaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CitaControllerTest {

    @Mock
    private CitaService citaService;

    @InjectMocks
    private CitaController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // -------- obtenerCitas --------
    @Test
    void obtenerCitas_delegaEnServicio() {
        when(citaService.obtenerCitas()).thenReturn(Arrays.asList(new Cita(), new Cita()));

        List<Cita> result = controller.obtenerCitas();

        assertEquals(2, result.size());
        verify(citaService).obtenerCitas();
    }

    // -------- obtenerCitaPorId --------
    @Test
    void obtenerCitaPorId_delegaEnServicio() {
        Cita cita = new Cita();
        when(citaService.obtenerCitaPorId(10L)).thenReturn(cita);

        Cita out = controller.obtenerCitaPorId(10L);

        assertSame(cita, out);
        verify(citaService).obtenerCitaPorId(10L);
    }

    // -------- cancelarCita --------
    @Test
    void cancelarCita_delegaEnServicio() {
        controller.cancelarCita(7L);
        verify(citaService).cancelarCita(7L);
    }

    // -------- agendarCita: camino feliz --------
    @Test
    void agendarCita_horasValidas_llamaServicio() {
        Cita cita = new Cita();
        cita.setHoraInicio("08:00");
        cita.setHoraFin("09:30");

        controller.agendarCita(cita);

        verify(citaService).agendarCita(cita);
    }

    // -------- agendarCita: fuera de horario --------
    @Test
    void agendarCita_fueraDeHorario_lanzaExcepcion() {
        Cita cita = new Cita();
        cita.setHoraInicio("07:59"); // antes de 08:00
        cita.setHoraFin("09:00");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> controller.agendarCita(cita));
        assertTrue(ex.getMessage().contains("Las citas solo pueden ser"));
        verify(citaService, never()).agendarCita(any());
    }

    // -------- agendarCita: fin no posterior al inicio --------
    @Test
    void agendarCita_finNoPosterior_lanzaExcepcion() {
        Cita cita = new Cita();
        cita.setHoraInicio("10:00");
        cita.setHoraFin("10:00"); // no es posterior

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> controller.agendarCita(cita));
        assertTrue(ex.getMessage().contains("La hora de fin debe ser posterior"));
        verify(citaService, never()).agendarCita(any());
    }

    // -------- agendarCita: formato inválido --------
    @Test
    void agendarCita_formatoInvalido_lanzaExcepcion() {
        Cita cita = new Cita();
        cita.setHoraInicio("10");     // formato incorrecto
        cita.setHoraFin("10:30");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> controller.agendarCita(cita));
        assertTrue(ex.getMessage().contains("Formato de hora incorrecto"));
        verify(citaService, never()).agendarCita(any());
    }
}
