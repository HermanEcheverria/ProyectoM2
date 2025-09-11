package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class HistoriaTest {

    @Test
    void setId_y_getId() {
        Historia h = new Historia();
        Long id = 77L;

        h.setId(id);

        assertEquals(id, h.getId());
    }

    @Test
    void settersYGetters_generales() {
        Historia h = new Historia();

        h.setNombreEntidad("Hospital Central");
        h.setHistoria("Fundado en 1980...");
        h.setMeritos("Premio a la excelencia 2020");
        h.setLineaDelTiempo("1980: Fundación; 1995: Ampliación;");
        h.setStatus("PUBLISHED");
        h.setRejectionReason("N/A");
        h.setEditorEmail("editor@hospital.com");

        assertEquals("Hospital Central", h.getNombreEntidad());
        assertEquals("Fundado en 1980...", h.getHistoria());
        assertEquals("Premio a la excelencia 2020", h.getMeritos());
        assertEquals("1980: Fundación; 1995: Ampliación;", h.getLineaDelTiempo());
        assertEquals("PUBLISHED", h.getStatus());
        assertEquals("N/A", h.getRejectionReason());
        assertEquals("editor@hospital.com", h.getEditorEmail());
    }
}
