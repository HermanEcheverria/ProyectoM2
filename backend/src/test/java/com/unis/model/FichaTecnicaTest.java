package com.unis.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class FichaTecnicaTest {

    @Test
    void settersYGetters_cubrenTodosLosCampos() {
        FichaTecnica ft = new FichaTecnica();

        // Datos de prueba
        Long idFicha = 101L;
        Long idServicio = 202L;
        LocalDate fecha = LocalDate.of(2025, 8, 27);
        String historial = "Consulta general; Rayos X; Laboratorio";
        String numeroAf = "AF-998877";
        String codigoSeg = "PLAN-GT-123";
        String carnetSeg = "CARN-445566";
        PacienteFT paciente = new PacienteFT(); // no necesitamos setear IDs internos

        // Setters
        ft.setIdFicha(idFicha);
        ft.setIdServicio(idServicio);
        ft.setFechaCreacion(fecha);
        ft.setHistorialServicios(historial);
        ft.setNumeroAfiliacion(numeroAf);
        ft.setCodigoSeguro(codigoSeg);
        ft.setCarnetSeguro(carnetSeg);
        ft.setPaciente(paciente);

        // Getters (todos los marcados como faltantes en el reporte)
        assertEquals(idFicha, ft.getIdFicha());
        assertEquals(idServicio, ft.getIdServicio());
        assertEquals(fecha, ft.getFechaCreacion());
        assertEquals(historial, ft.getHistorialServicios());
        assertEquals(numeroAf, ft.getNumeroAfiliacion());
        assertEquals(codigoSeg, ft.getCodigoSeguro());
        assertEquals(carnetSeg, ft.getCarnetSeguro());
        assertSame(paciente, ft.getPaciente());
    }

    @Test
    void setPaciente_permitedNull_yGetterDevuelveNull() {
        FichaTecnica ft = new FichaTecnica();

        // Aseguramos que soporta null sin NPE y que getPaciente() lo refleja
        ft.setPaciente(null);
        assertNull(ft.getPaciente());
    }
}
