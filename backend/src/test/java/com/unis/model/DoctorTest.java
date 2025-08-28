package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

class DoctorTest {

    @Test
    void settersYGetters_claves_yListaCitas() {
        Doctor d = new Doctor();

        Long idDoctor = 999L;
        Long idUsuario = 123L;

        // lista de citas
        List<Cita> citas = new ArrayList<>();
        citas.add(new Cita());
        citas.add(new Cita());

        // --- Setters CLAVE que están al 0% ---
        d.setIdDoctor(idDoctor);
        d.setIdUsuario(idUsuario);
        d.setCitas(citas);

        // --- Getters CLAVE ---
        assertEquals(idDoctor, d.getIdDoctor());
        assertEquals(idUsuario, d.getIdUsuario());
        assertSame(citas, d.getCitas());
        assertEquals(2, d.getCitas().size());

        // No debe romper si la lista es reemplazada por otra o por null
        List<Cita> nuevas = new ArrayList<>();
        d.setCitas(nuevas);
        assertSame(nuevas, d.getCitas());
        assertEquals(0, d.getCitas().size());

        d.setCitas(null);
        assertNull(d.getCitas());
    }

    @Test
    void coberturaCamposBasicos_sumaLineas() {
        Doctor d = new Doctor();

        String apellido = "Ramírez";
        String documento = "DOC-001";
        Date fnac = new Date(1724710000000L);
        String genero = "M";
        String telefono = "5555-2222";
        String especialidad = "Pediatría";
        String colegiado = "COL-123";
        String horario = "Lun-Vie 08:00-16:00";
        Date fgrad = new Date(1609459200000L); // 2021-01-01
        String universidad = "USAC";
        Usuario u = new Usuario();

        d.setApellido(apellido);
        d.setDocumento(documento);
        d.setFechaNacimiento(fnac);
        d.setGenero(genero);
        d.setTelefono(telefono);
        d.setEspecialidad(especialidad);
        d.setNumeroColegiado(colegiado);
        d.setHorarioAtencion(horario);
        d.setFechaGraduacion(fgrad);
        d.setUniversidadGraduacion(universidad);
        d.setUsuario(u);

        assertEquals(apellido, d.getApellido());
        assertEquals(documento, d.getDocumento());
        assertEquals(fnac, d.getFechaNacimiento());
        assertEquals(genero, d.getGenero());
        assertEquals(telefono, d.getTelefono());
        assertEquals(especialidad, d.getEspecialidad());
        assertEquals(colegiado, d.getNumeroColegiado());
        assertEquals(horario, d.getHorarioAtencion());
        assertEquals(fgrad, d.getFechaGraduacion());
        assertEquals(universidad, d.getUniversidadGraduacion());
        assertSame(u, d.getUsuario());
    }
}
