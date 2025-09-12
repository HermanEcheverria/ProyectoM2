package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class PacienteAccTest {

    @Test
    void settersYGetters_cubrenCamposFaltantes_yBasicos() {
        PacienteAcc p = new PacienteAcc();

        Long idPaciente = 1001L;
        UserAcc usuario = new UserAcc();
        String apellido = "Lopez";
        String documento = "DPI-12345678";
        Date fechaNac = new Date(1724710000000L); // cualquier fecha fija
        String genero = "M";
        String telefono = "5555-0000";
        Long idHospital = 77L;

        // ---- Setters clave marcados en 0% ----
        p.setIdPaciente(idPaciente);
        p.setUsuario(usuario);
        p.setIdHospital(idHospital);

        // ---- Setters adicionales (suman líneas/métodos) ----
        p.setApellido(apellido);
        p.setDocumento(documento);
        p.setFechaNacimiento(fechaNac);
        p.setGenero(genero);
        p.setTelefono(telefono);

        // ---- Getters clave ----
        assertEquals(idPaciente, p.getIdPaciente());
        assertSame(usuario, p.getUsuario());
        assertEquals(idHospital, p.getIdHospital());

        // ---- Getters adicionales ----
        assertEquals(apellido, p.getApellido());
        assertEquals(documento, p.getDocumento());
        assertEquals(fechaNac, p.getFechaNacimiento());
        assertEquals(genero, p.getGenero());
        assertEquals(telefono, p.getTelefono());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull_enCamposClave() {
        PacienteAcc p = new PacienteAcc();

        p.setUsuario(null);
        p.setIdHospital(null);

        assertNull(p.getUsuario());
        assertNull(p.getIdHospital());
    }
}
