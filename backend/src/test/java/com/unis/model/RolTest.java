package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RolTest {

    @Test
    void settersYGetters_debenAsignarYRetornarValores() {
        Rol rol = new Rol();

        Long id = 1L;
        String roleName = "ADMIN";

        rol.setId(id);
        rol.setRoleName(roleName);

        assertEquals(id, rol.getId());
        assertEquals(roleName, rol.getRoleName());
    }

    @Test
    void settersAceptanNull_yGettersDevuelvenNull() {
        Rol rol = new Rol();

        rol.setId(null);
        rol.setRoleName(null);

        assertNull(rol.getId());
        assertNull(rol.getRoleName());
    }
}
