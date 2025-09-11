package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;

class PacienteTest {

    @Test
    void settersYGetters_cubrenCamposSimples_yColecciones() {
        Paciente p = new Paciente();

        Long idPaciente = 123L;
        Long idUsuario = 456L;
        String apellido = "Perez";
        String documento = "DPI-0101";
        Date fechaNac = new Date(1724726400000L); // 2024-08-27 00:00:00 GMT aprox
        String genero = "M";
        String telefono = "5555-0000";
        byte[] foto = new byte[] {10, 20, 30};

        Usuario u = new Usuario();
        List<Cita> citas = new ArrayList<>();
        citas.add(new Cita());

        // Setters
        p.setIdPaciente(idPaciente);
        p.setIdUsuario(idUsuario);
        p.setApellido(apellido);
        p.setDocumento(documento);
        p.setFechaNacimiento(fechaNac);
        p.setGenero(genero);
        p.setTelefono(telefono);
        p.setFotografia(foto);
        p.setUsuario(u);
        p.setCitas(citas);

        // Getters
        assertEquals(idPaciente, p.getIdPaciente());
        assertEquals(idUsuario, p.getIdUsuario());
        assertEquals(apellido, p.getApellido());
        assertEquals(documento, p.getDocumento());
        assertEquals(fechaNac, p.getFechaNacimiento());
        assertEquals(genero, p.getGenero());
        assertEquals(telefono, p.getTelefono());
        assertTrue(Arrays.equals(foto, p.getFotografia()));
        assertSame(u, p.getUsuario());
        assertSame(citas, p.getCitas());
        assertEquals(1, p.getCitas().size());
    }

    @Test
    void getNombre_conDocumento_noNulo() {
        Paciente p = new Paciente();
        p.setApellido("Lopez");
        p.setDocumento("DPI-999");

        // Rama: documento != null
        assertEquals("Lopez DPI-999", p.getNombre());
    }

    @Test
    void getNombre_conDocumento_null() {
        Paciente p = new Paciente();
        p.setApellido("Ramirez");
        p.setDocumento(null);

        // Rama: documento == null (usa cadena vacía)
        assertEquals("Ramirez ", p.getNombre());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull() {
        Paciente p = new Paciente();

        p.setIdUsuario(null);
        p.setFotografia(null);
        p.setCitas(null); // permitir reemplazar la lista

        assertNull(p.getIdUsuario());
        assertNull(p.getFotografia());
        assertNull(p.getCitas());
    }
}
