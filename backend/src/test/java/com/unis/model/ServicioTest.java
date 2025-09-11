package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ServicioTest {

    @Test
    void getId_y_getIdServicio_devuelvenElMismoId() {
        Servicio s = new Servicio();
        s.id = 10L; // PanacheEntity expone 'id' como público

        assertEquals(10L, s.getId());
        assertEquals(10L, s.getIdServicio());
    }

    @Test
    void getParentId_conPadreYsinPadre() {
        Servicio padre = new Servicio();
        padre.id = 99L;

        Servicio hijo = new Servicio();
        hijo.servicioPadre = padre;

        assertEquals(99L, hijo.getParentId(), "Debe devolver el id del padre");
        hijo.servicioPadre = null;
        assertNull(hijo.getParentId(), "Si no hay padre, debe devolver null");
    }

    @Test
    void equals_mismaInstancia_true() {
        Servicio s = new Servicio();
        s.id = 1L;
        assertTrue(s.equals(s));
    }

    @Test
    void equals_objetoDeOtroTipo_false() {
        Servicio s = new Servicio();
        s.id = 1L;
        assertFalse(s.equals("no-servicio"));
    }

    @Test
    void equals_idsIgualesYNoNulos_true() {
        Servicio a = new Servicio();
        Servicio b = new Servicio();
        a.id = 7L;
        b.id = 7L;

        assertTrue(a.equals(b));
        assertTrue(b.equals(a));
        assertEquals(a.hashCode(), b.hashCode(), "hashCode debe coincidir cuando los ids coinciden");
    }

    @Test
    void equals_idNull_enThis_false_y_hashCodeCero() {
        Servicio a = new Servicio();
        Servicio b = new Servicio();
        a.id = null;
        b.id = 2L;

        assertFalse(a.equals(b), "Si this.id es null, equals debe ser false");
        assertEquals(0, a.hashCode(), "Con id null, hashCode debe ser 0");
    }

    @Test
    void equals_idsDistintos_false() {
        Servicio a = new Servicio();
        Servicio b = new Servicio();
        a.id = 3L;
        b.id = 4L;

        assertFalse(a.equals(b));
        assertNotEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void equals_ambosIdNull_false() {
        Servicio a = new Servicio();
        Servicio b = new Servicio();
        a.id = null;
        b.id = null;

        assertFalse(a.equals(b), "Con ambos id null, equals debe ser false por la condición id != null");
        assertEquals(0, a.hashCode());
        assertEquals(0, b.hashCode());
    }
}
