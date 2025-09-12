package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AseguradoraTest {

    @Test
    void settersYGetters_cubrenIdYNombre() {
        Aseguradora a = new Aseguradora();

        Long id = 15L;
        String nombre = "Seguro Nacional";

        a.setId(id);
        a.setNombre(nombre);

        assertEquals(id, a.getId());
        assertEquals(nombre, a.getNombre());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull() {
        Aseguradora a = new Aseguradora();

        a.setId(null);
        a.setNombre(null);

        assertNull(a.getId());
        assertNull(a.getNombre());
    }
}
