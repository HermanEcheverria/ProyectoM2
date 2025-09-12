package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

import org.junit.jupiter.api.Test;

class RecetaTest {

    @Test
    void settersYGettersBasicos_debenAsignarYRetornarValores() {
        Receta receta = new Receta();
        Long idCita = 123L;

        // set y get de idCita
        receta.setIdCita(idCita);
        assertEquals(idCita, receta.getIdCita());
    }

    @Test
    void getPaciente_debeRetornarEntidadAsignada() {
        Receta receta = new Receta();
        Paciente paciente = new Paciente();
        paciente.setIdPaciente(77L);

        // inyectamos el paciente manualmente
        receta.setIdPaciente(77L);
        // usamos reflexión o acceso directo si el campo es package-private
        // pero aquí basta con asignar por setter JPA simulado
        try {
            var field = Receta.class.getDeclaredField("paciente");
            field.setAccessible(true);
            field.set(receta, paciente);
        } catch (Exception e) {
            fail("No se pudo inyectar el paciente en la receta");
        }

        assertNotNull(receta.getPaciente());
        assertEquals(77L, receta.getPaciente().getIdPaciente());
    }

    @Test
    void settersAceptanNull_yGettersDevuelvenNull() {
        Receta receta = new Receta();

        receta.setIdCita(null);
        assertNull(receta.getIdCita());

        // paciente null
        assertNull(receta.getPaciente());
    }
}
