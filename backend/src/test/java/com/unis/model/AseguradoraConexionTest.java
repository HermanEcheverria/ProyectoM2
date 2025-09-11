package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AseguradoraConexionTest {

    @Test
    void setId_y_getId() {
        AseguradoraConexion conexion = new AseguradoraConexion();
        Long id = 100L;

        conexion.setId(id);

        assertEquals(id, conexion.getId());
    }

    @Test
    void setters_y_getters_basicos() {
        AseguradoraConexion conexion = new AseguradoraConexion();

        conexion.setNombre("Seguros Nacionales");
        conexion.setUrlBase("https://api.segurosnacionales.com");

        assertEquals("Seguros Nacionales", conexion.getNombre());
        assertEquals("https://api.segurosnacionales.com", conexion.getUrlBase());
    }

    @Test
    void settersAceptanNull_yGettersDevuelvenNull() {
        AseguradoraConexion conexion = new AseguradoraConexion();

        conexion.setId(null);
        conexion.setNombre(null);
        conexion.setUrlBase(null);

        assertNull(conexion.getId());
        assertNull(conexion.getNombre());
        assertNull(conexion.getUrlBase());
    }
}
