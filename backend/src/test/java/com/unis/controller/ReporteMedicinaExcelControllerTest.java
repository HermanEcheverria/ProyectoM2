package com.unis.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.unis.service.ReporteMedicinaExcelService;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

/**
 * Tests for ReporteMedicinaExcelController
 */
class ReporteMedicinaExcelControllerTest {

    private ReporteMedicinaExcelController controller;
    private ReporteMedicinaExcelService excelService;

    private static final String CT_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @BeforeEach
    void setUp() {
        controller = new ReporteMedicinaExcelController();
        excelService = mock(ReporteMedicinaExcelService.class);
        // El campo excelService NO es private en el controlador, así que podemos asignarlo.
        controller.excelService = excelService;
    }

    @Test
    void descargarExcel_ok_devuelve200_yExcel() throws Exception {
        // given
        String inicio = "2025-01-01";
        String fin = "2025-01-31";
        int limite = 25;
        String usuario = "reporter@demo.com";

        byte[] fakeExcel = new byte[] { 1, 2, 3 };
        when(excelService.generarExcel(any(Date.class), any(Date.class), eq(limite), eq(usuario)))
                .thenReturn(fakeExcel);

        // when
        Response resp = controller.descargarExcel(inicio, fin, limite, usuario);

        // then
        assertEquals(200, resp.getStatus());
        assertEquals(CT_XLSX, resp.getHeaderString("Content-Type"));
        assertNotNull(resp.getHeaderString("Content-Disposition"));
        assertTrue(resp.getHeaderString("Content-Disposition").contains("medicinas_reporte_2025-01-01_a_2025-01-31.xlsx"));

        assertArrayEquals(fakeExcel, (byte[]) resp.getEntity());

        // Verifica parámetros exactos enviados al servicio
        ArgumentCaptor<Date> capIni = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> capFin = ArgumentCaptor.forClass(Date.class);
        verify(excelService).generarExcel(capIni.capture(), capFin.capture(), eq(limite), eq(usuario));

        assertEquals(sdf.parse(inicio), capIni.getValue());
        assertEquals(sdf.parse(fin), capFin.getValue());
    }

    @Test
    void descargarExcel_faltaInicio_retorna400() {
        Response resp = controller.descargarExcel(null, "2025-01-31", 10, "x@y.com");
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("inicio"));
    }

    @Test
    void descargarExcel_faltaFin_retorna400() {
        Response resp = controller.descargarExcel("2025-01-01", null, 10, "x@y.com");
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("fin"));
    }

    @Test
    void descargarExcel_formatoInvalido_retorna400() {
        Response resp = controller.descargarExcel("2025/01/01", "2025-01-31", 10, "x@y.com");
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("Formato de fecha inválido"));
    }

    @Test
    void descargarExcel_rangoInvalido_retorna400() {
        Response resp = controller.descargarExcel("2025-02-01", "2025-01-01", 10, "x@y.com");
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("inicio"));
    }

    @Test
    void descargarExcel_limiteInvalido_retorna400() {
        Response resp = controller.descargarExcel("2025-01-01", "2025-01-31", 0, "x@y.com");
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("limite"));
    }

    @Test
    void descargarExcel_usuarioVacio_normalizaADefault() throws Exception {
        String inicio = "2025-01-01";
        String fin = "2025-01-31";
        int limite = 5;
        String usuarioVacio = "   "; // se normaliza a admin@hospital.com por el controlador
        byte[] fakeExcel = new byte[] { 9, 9 };

        when(excelService.generarExcel(any(Date.class), any(Date.class), eq(limite), eq("admin@hospital.com")))
                .thenReturn(fakeExcel);

        Response resp = controller.descargarExcel(inicio, fin, limite, usuarioVacio);

        assertEquals(200, resp.getStatus());
        assertArrayEquals(fakeExcel, (byte[]) resp.getEntity());

        // Verifica que llamó al servicio con el usuario normalizado
        verify(excelService).generarExcel(any(Date.class), any(Date.class), eq(limite), eq("admin@hospital.com"));
    }
}
