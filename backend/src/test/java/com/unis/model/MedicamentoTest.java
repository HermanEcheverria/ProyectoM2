package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MedicamentoTest {

    @Test
    void settersYGetters_cubrenTodosLosCampos() {
        Medicamento m = new Medicamento();

        Long id = 77L;
        String principio = "Paracetamol";
        String concentracion = "500mg";
        String presentacion = "Tableta";
        String forma = "Sólida";
        Integer ventaLibre = 1; // 1 = OTC, 0 = con receta

        // Setters
        m.setIdMedicamento(id);
        m.setPrincipioActivo(principio);
        m.setConcentracion(concentracion);
        m.setPresentacion(presentacion);
        m.setFormaFarmaceutica(forma);
        m.setVentaLibre(ventaLibre);

        // Getters
        assertEquals(id, m.getIdMedicamento());
        assertEquals(principio, m.getPrincipioActivo());
        assertEquals(concentracion, m.getConcentracion());
        assertEquals(presentacion, m.getPresentacion());
        assertEquals(forma, m.getFormaFarmaceutica());
        assertEquals(ventaLibre, m.getVentaLibre());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull() {
        Medicamento m = new Medicamento();

        m.setPrincipioActivo(null);
        m.setConcentracion(null);
        m.setPresentacion(null);
        m.setFormaFarmaceutica(null);
        m.setVentaLibre(null);

        assertNull(m.getPrincipioActivo());
        assertNull(m.getConcentracion());
        assertNull(m.getPresentacion());
        assertNull(m.getFormaFarmaceutica());
        assertNull(m.getVentaLibre());
    }
}
