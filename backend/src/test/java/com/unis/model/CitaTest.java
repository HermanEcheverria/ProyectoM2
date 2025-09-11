package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class CitaTest {

    @Test
    void settersYGettersSimples_cubrenCamposFaltantes() {
        Cita cita = new Cita();

        // --- IDs y strings simples ---
        cita.setIdCita(100L);
        cita.setNumeroAutorizacion("AUT-999");
        cita.setHoraInicio("08:30");
        cita.setHoraFin("09:00");
        cita.setMotivo("Control general");
        cita.setDiagnostico("Sin hallazgos");
        cita.setResultados("OK");

        // --- LocalDate ---
        LocalDate fecha = LocalDate.of(2025, 8, 27);
        cita.setFecha(fecha);

        // --- IDs relacionados (primitivos en Cita) ---
        cita.setIdHospital(10L);
        cita.setIdServicio(20L);
        cita.setIdAseguradora(30L);

        // --- Estado enum ---
        cita.setEstado(EstadoCita.CONFIRMADA);

        // --- Entidades relacionadas (solo referenciar objetos, sin setear IDs internos) ---
        Hospital hospital = new Hospital();
        Servicio servicio = new Servicio();
        Aseguradora aseguradora = new Aseguradora();

        cita.setHospital(hospital);
        cita.setServicio(servicio);
        cita.setAseguradora(aseguradora);

        // ====== Asserts sobre getters marcados como faltantes ======
        assertEquals(100L, cita.getIdCita());
        assertEquals("AUT-999", cita.getNumeroAutorizacion());
        assertEquals(fecha, cita.getFecha());
        assertEquals(10L, cita.getIdHospital());
        assertEquals(20L, cita.getIdServicio());
        assertEquals(30L, cita.getIdAseguradora());
        assertEquals("Control general", cita.getMotivo());
        assertSame(hospital, cita.getHospital());
        assertSame(servicio, cita.getServicio());
        assertSame(aseguradora, cita.getAseguradora());

        // Otros getters para robustez
        assertEquals("08:30", cita.getHoraInicio());
        assertEquals("09:00", cita.getHoraFin());
        assertEquals(EstadoCita.CONFIRMADA, cita.getEstado());
        assertEquals("Sin hallazgos", cita.getDiagnostico());
        assertEquals("OK", cita.getResultados());
    }

    @Test
    void setDoctor_noNulo_debeAsignarEntidadYPropagarIdDoctor() {
        Cita cita = new Cita();

        // Valor previo para comprobar que se sobrescribe
        cita.setIdDoctor(999L);

        // Mockeamos Doctor para controlar getIdDoctor()
        Doctor doctor = mock(Doctor.class);
        when(doctor.getIdDoctor()).thenReturn(123L);

        cita.setDoctor(doctor);

        assertSame(doctor, cita.getDoctor());
        assertEquals(123L, cita.getIdDoctor(), "setDoctor debe propagar doctor.getIdDoctor() a idDoctor");
    }

    @Test
    void setDoctor_nulo_noDebeAlterarIdDoctor() {
        Cita cita = new Cita();
        cita.setIdDoctor(777L); // valor inicial

        cita.setDoctor(null);   // rama nula

        assertNull(cita.getDoctor());
        assertEquals(777L, cita.getIdDoctor(), "Si doctor es null, idDoctor debe conservarse");
    }

    @Test
    void setPaciente_noNulo_debeAsignarEntidadYPropagarIdPaciente() {
        Cita cita = new Cita();

        // Valor previo para comprobar que se sobrescribe
        cita.setIdPaciente(888L);

        // Mockeamos Paciente para controlar getIdPaciente()
        Paciente paciente = mock(Paciente.class);
        when(paciente.getIdPaciente()).thenReturn(456L);

        cita.setPaciente(paciente);

        assertSame(paciente, cita.getPaciente());
        assertEquals(456L, cita.getIdPaciente(), "setPaciente debe propagar paciente.getIdPaciente() a idPaciente");
    }

    @Test
    void setPaciente_nulo_noDebeAlterarIdPaciente() {
        Cita cita = new Cita();
        cita.setIdPaciente(555L); // valor inicial

        cita.setPaciente(null);   // rama nula

        assertNull(cita.getPaciente());
        assertEquals(555L, cita.getIdPaciente(), "Si paciente es null, idPaciente debe conservarse");
    }
}
