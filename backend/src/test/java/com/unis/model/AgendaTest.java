package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AgendaTest {

    @Test
    void setIdDoctor_y_gettersBasicos() {
        Agenda a = new Agenda();

        Long idDoctor = 42L;
        a.setIdDoctor(idDoctor);

        assertEquals(idDoctor, a.getIdDoctor());
        assertNull(a.getId()); // id es @GeneratedValue, aún no seteado por JPA
    }

    @Test
    void getId_porReflection_paraCubrirGetter() throws Exception {
        Agenda a = new Agenda();

        // Forzamos valor al campo 'id' privado para cubrir getId()
        var f = Agenda.class.getDeclaredField("id");
        f.setAccessible(true);
        f.set(a, 999L);

        assertEquals(999L, a.getId());
    }

    @Test
    void settersAdicionales_soloParaSumarLineas() {
        Agenda a = new Agenda();
        a.setDiasAtencion("Mon,Tue,Wed");
        a.setHoraInicio("08:00");
        a.setHoraFin("16:30");
        a.setNotas("Solo mañana los viernes");

        assertEquals("Mon,Tue,Wed", a.getDiasAtencion());
        assertEquals("08:00", a.getHoraInicio());
        assertEquals("16:30", a.getHoraFin());
        assertEquals("Solo mañana los viernes", a.getNotas());
    }
}
