package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class PacienteFTTest {

    @Test
    void settersYGetters_cubrenTodosLosCampos() {
        PacienteFT p = new PacienteFT();

        Long idPaciente = 1001L;
        Long idUsuario = 2002L;
        String documento = "DPI-1234567890101";
        LocalDate fnac = LocalDate.of(2000, 5, 15);
        byte[] foto = new byte[] {1, 2, 3, 4, 5};

        Usuario usuario = new Usuario(); // no es necesario setearle campos
        List<FichaTecnica> fichas = new ArrayList<>();
        fichas.add(new FichaTecnica());

        // Setters
        p.setIdPaciente(idPaciente);
        p.setIdUsuario(idUsuario);
        p.setDocumento(documento);
        p.setFechaNacimiento(fnac);
        p.setFotografia(foto);
        p.setUsuario(usuario);
        p.setFichasTecnicas(fichas);

        // Getters (todos los que JaCoCo marcó con 0%)
        assertEquals(idPaciente, p.getIdPaciente());
        assertEquals(idUsuario, p.getIdUsuario());
        assertEquals(documento, p.getDocumento());
        assertEquals(fnac, p.getFechaNacimiento());
        assertTrue(Arrays.equals(foto, p.getFotografia()));
        assertSame(usuario, p.getUsuario());
        assertSame(fichas, p.getFichasTecnicas());
        assertEquals(1, p.getFichasTecnicas().size());
    }

    @Test
    void settersAceptanNull_yGettersReflejanNull() {
        PacienteFT p = new PacienteFT();

        p.setDocumento(null);
        p.setFechaNacimiento(null);
        p.setFotografia(null);
        p.setUsuario(null);
        p.setFichasTecnicas(null);

        assertNull(p.getDocumento());
        assertNull(p.getFechaNacimiento());
        assertNull(p.getFotografia());
        assertNull(p.getUsuario());
        assertNull(p.getFichasTecnicas());
    }

    @Test
    void idUsuario_esIndependienteDeUsuarioEntidad() {
        // Mapea a la misma columna con insertable=false, updatable=false en la entidad Usuario
        // Verificamos que setIdUsuario y setUsuario no interfieren entre sí
        PacienteFT p = new PacienteFT();

        p.setIdUsuario(999L);
        Usuario u = new Usuario();
        p.setUsuario(u);

        assertEquals(999L, p.getIdUsuario());
        assertSame(u, p.getUsuario());

        // Cambiar la referencia de usuario no debe cambiar el idUsuario
        Usuario u2 = new Usuario();
        p.setUsuario(u2);
        assertEquals(999L, p.getIdUsuario());
        assertSame(u2, p.getUsuario());
    }
}
