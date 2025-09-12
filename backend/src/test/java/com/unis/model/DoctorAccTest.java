package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class DoctorAccTest {

    @Test
    void settersYGetters_cubrenCamposFaltantesYBasicos() {
        DoctorAcc d = new DoctorAcc();

        Long idDoctor = 101L;
        UserAcc usuario = new UserAcc();
        Long idHospital = 55L;
        String horario = "Lun-Vie 08:00-16:00";

        // Otros campos (ya cubiertos según el reporte, pero suman líneas)
        String apellido = "Sanchez";
        String documento = "COL-12345";
        Date fechaNac = new Date(1724700000000L); // cualquier fecha válida
        String genero = "M";
        String telefono = "5555-6789";
        String especialidad = "Cardiología";
        String numeroColegiado = "NC-0001";
        Date fechaGrad = new Date(1609459200000L); // 2021-01-01
        String universidad = "USAC";
        String disponibilidad = "Tiempo completo";

        // ---- Setters clave (faltantes en cobertura) ----
        d.setIdDoctor(idDoctor);
        d.setUsuario(usuario);
        d.setIdHospital(idHospital);
        d.setHorarioAtencion(horario);

        // ---- Setters adicionales ----
        d.setApellido(apellido);
        d.setDocumento(documento);
        d.setFechaNacimiento(fechaNac);
        d.setGenero(genero);
        d.setTelefono(telefono);
        d.setEspecialidad(especialidad);
        d.setNumeroColegiado(numeroColegiado);
        d.setFechaGraduacion(fechaGrad);
        d.setUniversidadGraduacion(universidad);
        d.setDisponibilidad(disponibilidad);

        // ---- Getters clave ----
        assertEquals(idDoctor, d.getIdDoctor());
        assertSame(usuario, d.getUsuario());
        assertEquals(idHospital, d.getIdHospital());
        assertEquals(horario, d.getHorarioAtencion());

        // ---- Getters adicionales ----
        assertEquals(apellido, d.getApellido());
        assertEquals(documento, d.getDocumento());
        assertEquals(fechaNac, d.getFechaNacimiento());
        assertEquals(genero, d.getGenero());
        assertEquals(telefono, d.getTelefono());
        assertEquals(especialidad, d.getEspecialidad());
        assertEquals(numeroColegiado, d.getNumeroColegiado());
        assertEquals(fechaGrad, d.getFechaGraduacion());
        assertEquals(universidad, d.getUniversidadGraduacion());
        assertEquals(disponibilidad, d.getDisponibilidad());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull_enCamposClave() {
        DoctorAcc d = new DoctorAcc();

        d.setUsuario(null);
        d.setHorarioAtencion(null);

        assertNull(d.getUsuario());
        assertNull(d.getHorarioAtencion());
    }
}
