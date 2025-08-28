package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class HospitalTest {

    @Test
    void settersYGetters_cubrenTodosLosCampos() {
        Hospital h = new Hospital();

        Long id = 42L;
        String nombre = "Hospital Central";
        String direccion = "Av. Reforma 123, Zona 10";
        String telefono = "2222-3333";
        String correo = "contacto@hospital.gt";
        String estado = "A"; // por ejemplo: Activo
        LocalDateTime fechaCreacion = LocalDateTime.of(2025, 8, 27, 19, 0, 0);
        String mongoId = "64f1a7c2b9a012345678abcd";

        // Setters
        h.setId(id);
        h.setNombre(nombre);
        h.setDireccion(direccion);
        h.setTelefono(telefono);
        h.setCorreo(correo);
        h.setEstado(estado);
        h.setFechaCreacion(fechaCreacion);
        h.setMongoId(mongoId);

        // Getters (todos los marcados como faltantes)
        assertEquals(id, h.getId());
        assertEquals(nombre, h.getNombre());
        assertEquals(direccion, h.getDireccion());
        assertEquals(telefono, h.getTelefono());
        assertEquals(correo, h.getCorreo());
        assertEquals(estado, h.getEstado());
        assertEquals(fechaCreacion, h.getFechaCreacion());
        assertEquals(mongoId, h.getMongoId());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull() {
        Hospital h = new Hospital();

        // Asignar nulls para asegurar que no hay NPEs y que getters reflejan el valor
        h.setNombre(null);
        h.setDireccion(null);
        h.setTelefono(null);
        h.setCorreo(null);
        h.setEstado(null);
        h.setFechaCreacion(null);
        h.setMongoId(null);

        assertNull(h.getNombre());
        assertNull(h.getDireccion());
        assertNull(h.getTelefono());
        assertNull(h.getCorreo());
        assertNull(h.getEstado());
        assertNull(h.getFechaCreacion());
        assertNull(h.getMongoId());
    }
}
