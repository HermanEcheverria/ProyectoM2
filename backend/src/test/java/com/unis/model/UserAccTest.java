package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class UserAccTest {

    @Test
    void settersYGetters_cubrenCamposFaltantes_yBasicos() {
        UserAcc u = new UserAcc();

        Long idUsuario = 100L;
        String nombre = "andres";
        String contrasena = "hash$123";
        int rolId = 3;
        String correo = "andres@example.com";
        int estado = 1; // activo
        Date fecha = new Date(1724712345000L); // cualquier fecha estable
        Long idHospital = 77L;

        // ----- Setters clave faltantes en cobertura -----
        u.setIdUsuario(idUsuario);
        u.setEstado(estado);
        u.setFechaCreacion(fecha);
        u.setIdHospital(idHospital);

        // ----- Setters ya cubiertos, suman líneas -----
        u.setNombreUsuario(nombre);
        u.setContrasena(contrasena);
        u.setRolId(rolId);
        u.setCorreo(correo);

        // ----- Getters (todos los marcados en el reporte) -----
        assertEquals(idUsuario, u.getIdUsuario());
        assertEquals(estado, u.getEstado());
        assertEquals(fecha, u.getFechaCreacion());
        assertEquals(idHospital, u.getIdHospital());

        assertEquals(nombre, u.getNombreUsuario());
        assertEquals(contrasena, u.getContrasena());
        assertEquals(rolId, u.getRolId());
        assertEquals(correo, u.getCorreo());
    }

    @Test
    void settersAceptanNull_enCamposReferencia_yGettersReflejanNull() {
        UserAcc u = new UserAcc();

        u.setIdUsuario(null);
        u.setFechaCreacion(null);
        u.setIdHospital(null);
        u.setCorreo(null);
        u.setContrasena(null);
        u.setNombreUsuario(null);

        assertNull(u.getIdUsuario());
        assertNull(u.getFechaCreacion());
        assertNull(u.getIdHospital());
        assertNull(u.getCorreo());
        assertNull(u.getContrasena());
        assertNull(u.getNombreUsuario());

        // estado y rolId son primitivos (int), no admiten null, pero pueden cambiarse
        u.setEstado(0);
        u.setRolId(0);
        assertEquals(0, u.getEstado());
        assertEquals(0, u.getRolId());
    }
}
