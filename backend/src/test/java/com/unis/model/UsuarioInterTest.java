package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class UsuarioInterTest {

    @Test
    void settersYGetters_cubrenTodosLosCampos() {
        UsuarioInter ui = new UsuarioInter();

        Long idInter = 321L;
        String apellido = "García";
        String documento = "DPI-1234567890101";
        Date fechaNac = new Date(1724710000000L); // cualquier fecha válida
        String genero = "F";
        String telefono = "5555-1234";
        Long idHospital = 42L;
        Usuario usuario = new Usuario();

        // Setters
        ui.setIdInterconexion(idInter);
        ui.setApellido(apellido);
        ui.setDocumento(documento);
        ui.setFechaNacimiento(fechaNac);
        ui.setGenero(genero);
        ui.setTelefono(telefono);
        ui.setIdHospital(idHospital);
        ui.setUsuario(usuario);

        // Getters
        assertEquals(idInter, ui.getIdInterconexion());
        assertEquals(apellido, ui.getApellido());
        assertEquals(documento, ui.getDocumento());
        assertEquals(fechaNac, ui.getFechaNacimiento());
        assertEquals(genero, ui.getGenero());
        assertEquals(telefono, ui.getTelefono());
        assertEquals(idHospital, ui.getIdHospital());
        assertSame(usuario, ui.getUsuario());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull() {
        UsuarioInter ui = new UsuarioInter();

        ui.setApellido(null);
        ui.setDocumento(null);
        ui.setFechaNacimiento(null);
        ui.setGenero(null);
        ui.setTelefono(null);
        ui.setIdHospital(null);
        ui.setUsuario(null);

        assertNull(ui.getApellido());
        assertNull(ui.getDocumento());
        assertNull(ui.getFechaNacimiento());
        assertNull(ui.getGenero());
        assertNull(ui.getTelefono());
        assertNull(ui.getIdHospital());
        assertNull(ui.getUsuario());
    }
}
