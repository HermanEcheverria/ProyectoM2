package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class UsuarioInterAccTest {

    @Test
    void settersYGetters_cubrenCamposFaltantes_yBasicos() {
        UsuarioInterAcc ui = new UsuarioInterAcc();

        Long idInter = 321L;
        Usuario usuario = new Usuario();
        String apellido = "García";
        String documento = "DPI-12345678-0101";
        Date fechaNac = new Date(1724710000000L); // fecha fija
        String genero = "F";
        String telefono = "5555-1234";
        Long idHospital = 77L;

        // ---- Setters CLAVE que JaCoCo marcó al 0% ----
        ui.setIdInterconexion(idInter);
        ui.setUsuario(usuario);

        // ---- Setters adicionales (suman líneas/métodos) ----
        ui.setApellido(apellido);
        ui.setDocumento(documento);
        ui.setFechaNacimiento(fechaNac);
        ui.setGenero(genero);
        ui.setTelefono(telefono);
        ui.setIdHospital(idHospital);

        // ---- Getters CLAVE ----
        assertEquals(idInter, ui.getIdInterconexion());
        assertSame(usuario, ui.getUsuario());

        // ---- Getters adicionales ----
        assertEquals(apellido, ui.getApellido());
        assertEquals(documento, ui.getDocumento());
        assertEquals(fechaNac, ui.getFechaNacimiento());
        assertEquals(genero, ui.getGenero());
        assertEquals(telefono, ui.getTelefono());
        assertEquals(idHospital, ui.getIdHospital());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull_enCamposClave() {
        UsuarioInterAcc ui = new UsuarioInterAcc();

        ui.setUsuario(null);
        ui.setIdInterconexion(null);

        assertNull(ui.getUsuario());
        assertNull(ui.getIdInterconexion());
    }
}
