package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class EmpleadoAccTest {

    @Test
    void settersYGetters_cubrenCamposFaltantes_yBasicos() {
        EmpleadoAcc e = new EmpleadoAcc();

        Long idEmpleado = 901L;
        UserAcc usuario = new UserAcc();
        Long idHospital = 77L;

        // Otros campos para sumar líneas/métodos
        String apellido = "Gómez";
        String documento = "EMP-001";
        Date fechaNac = new Date(1724710000000L); // cualquier fecha válida
        String genero = "F";
        String telefono = "5555-9090";
        String puesto = "Administrador";

        // ---- Setters clave que JaCoCo marcó en 0% ----
        e.setIdEmpleado(idEmpleado);
        e.setUsuario(usuario);
        e.setIdHospital(idHospital);

        // ---- Setters adicionales ----
        e.setApellido(apellido);
        e.setDocumento(documento);
        e.setFechaNacimiento(fechaNac);
        e.setGenero(genero);
        e.setTelefono(telefono);
        e.setPuesto(puesto);

        // ---- Getters clave ----
        assertEquals(idEmpleado, e.getIdEmpleado());
        assertSame(usuario, e.getUsuario());
        assertEquals(idHospital, e.getIdHospital());

        // ---- Getters adicionales ----
        assertEquals(apellido, e.getApellido());
        assertEquals(documento, e.getDocumento());
        assertEquals(fechaNac, e.getFechaNacimiento());
        assertEquals(genero, e.getGenero());
        assertEquals(telefono, e.getTelefono());
        assertEquals(puesto, e.getPuesto());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull_enCamposClave() {
        EmpleadoAcc e = new EmpleadoAcc();

        e.setUsuario(null);
        e.setIdHospital(null);

        assertNull(e.getUsuario());
        assertNull(e.getIdHospital());
    }
}
