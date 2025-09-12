package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class RecetaMedicamentoTest {

    @Test
    void settersYGetters_simples() {
        RecetaMedicamento rm = new RecetaMedicamento();

        Long idRM = 1L;
        String dosis = "500mg";
        String frecuencia = "cada 8 horas";
        String duracion = "7 días";
        String diagnostico = "Gripe";

        // Setters
        rm.setIdRecetaMedicamento(idRM);
        rm.setDosis(dosis);
        rm.setFrecuencia(frecuencia);
        rm.setDuracion(duracion);
        rm.setDiagnostico(diagnostico);

        // Getters
        assertEquals(idRM, rm.getIdRecetaMedicamento());
        assertEquals(dosis, rm.getDosis());
        assertEquals(frecuencia, rm.getFrecuencia());
        assertEquals(duracion, rm.getDuracion());
        assertEquals(diagnostico, rm.getDiagnostico());
    }

    @Test
    void getIdReceta_conRecetaNoNula_devuelveId() {
        RecetaMedicamento rm = new RecetaMedicamento();

        // Mock de Receta para controlar getIdReceta()
        Receta receta = mock(Receta.class);
        when(receta.getIdReceta()).thenReturn(77L);

        rm.setReceta(receta);

        assertSame(receta, rm.getReceta());
        assertEquals(77L, rm.getIdReceta());
    }

    @Test
    void getIdReceta_conRecetaNull_devuelveNull() {
        RecetaMedicamento rm = new RecetaMedicamento();

        rm.setReceta(null);

        assertNull(rm.getReceta());
        assertNull(rm.getIdReceta());
    }

    @Test
    void getIdMedicamento_conMedicamentoNoNulo_devuelveId() {
        RecetaMedicamento rm = new RecetaMedicamento();

        // Mock de Medicamento para controlar getIdMedicamento()
        Medicamento med = mock(Medicamento.class);
        when(med.getIdMedicamento()).thenReturn(99L);

        rm.setMedicamento(med);

        assertSame(med, rm.getMedicamento());
        assertEquals(99L, rm.getIdMedicamento());
    }

    @Test
    void getIdMedicamento_conMedicamentoNull_devuelveNull() {
        RecetaMedicamento rm = new RecetaMedicamento();

        rm.setMedicamento(null);

        assertNull(rm.getMedicamento());
        assertNull(rm.getIdMedicamento());
    }
}
