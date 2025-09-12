package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class EmpleadoTest {

    @Test
    void setIdEmpleado_y_getIdEmpleado() {
        Empleado e = new Empleado();
        Long id = 123L;

        e.setIdEmpleado(id);

        assertEquals(id, e.getIdEmpleado());
    }

    @Test
    void setters_y_getters_basicos() {
        Empleado e = new Empleado();

        Usuario u = new Usuario();
        u.setId(9L);
        u.setNombreUsuario("emp_user");

        e.setUsuario(u);
        e.setApellido("García");
        e.setDocumento("DOC-777");
        Date fecha = new Date(1_700_000_000_000L);
        e.setFechaNacimiento(fecha);
        e.setGenero("M");
        e.setTelefono("555-1234");
        e.setPuesto("Administrador");

        assertSame(u, e.getUsuario());
        assertEquals("García", e.getApellido());
        assertEquals("DOC-777", e.getDocumento());
        assertEquals(fecha, e.getFechaNacimiento());
        assertEquals("M", e.getGenero());
        assertEquals("555-1234", e.getTelefono());
        assertEquals("Administrador", e.getPuesto());
    }
}
