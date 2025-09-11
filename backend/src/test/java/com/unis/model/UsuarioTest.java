package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void settersYGetters_cubrenCamposFaltantes_yDefaults() {
        Usuario u = new Usuario();

        // Defaults en el constructor
        assertEquals(0, u.getEstado(), "estado por defecto debe ser 0");
        assertNotNull(u.getFechaCreaction(), "fechaCreaction debe inicializarse por defecto");

        Long id = 99L;
        String nombreUsuario = "andres";
        String correo = "andres@acme.com";
        String contrasena = "hash123";
        Rol rol = new Rol();
        int estado = 1;
        Date fecha = new Date(1724712345000L); // cualquier fecha estable

        // Setters clave que faltaban en cobertura
        u.setId(id);
        u.setRol(rol);
        u.setEstado(estado);
        u.setFechaCreaction(fecha);

        // Setters ya cubiertos pero suman líneas
        u.setNombreUsuario(nombreUsuario);
        u.setCorreo(correo);
        u.setContrasena(contrasena);

        // Getters
        assertEquals(id, u.getId());
        assertSame(rol, u.getRol());
        assertEquals(estado, u.getEstado());
        assertEquals(fecha, u.getFechaCreaction());

        assertEquals(nombreUsuario, u.getNombreUsuario());
        assertEquals(correo, u.getCorreo());
        assertEquals(contrasena, u.getContrasena());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull() {
        Usuario u = new Usuario();

        u.setId(null);
        u.setRol(null);
        u.setFechaCreaction(null);

        assertNull(u.getId());
        assertNull(u.getRol());
        assertNull(u.getFechaCreaction());

        // estado es primitivo (int), no admite null; validamos que podamos setear otro valor
        u.setEstado(0);
        assertEquals(0, u.getEstado());
    }
}
