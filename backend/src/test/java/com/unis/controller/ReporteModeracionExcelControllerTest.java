package com.unis.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;

import com.unis.service.ReporteModeracionExcelService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.core.Response;

@ExtendWith(MockitoExtension.class)
class ReporteModeracionExcelControllerTest {

    @Mock
    ReporteModeracionExcelService excelService;

    @InjectMocks
    ReporteModeracionExcelController controller;

    private static final String CT_XLSX =
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    @BeforeEach
    void setup() {
        // nada por ahora
    }

    @Test
    void descargarExcel_ok_devuelve200_yExcel() throws Exception {
        byte[] fakeExcel = new byte[]{1, 2, 3};
        when(excelService.generarExcel(any(Date.class), any(Date.class), eq(5)))
                .thenReturn(fakeExcel);

        Response resp = controller.descargarExcel("2024-01-01", "2024-01-31", 5);

        assertEquals(200, resp.getStatus());
        // Content-Type explícito
        assertEquals(CT_XLSX, resp.getHeaderString("Content-Type"));
        // Content-Disposition con nombre de archivo que contiene el rango
        String cd = resp.getHeaderString("Content-Disposition");
        assertNotNull(cd);
        assertTrue(cd.contains("moderacion_reporte_2024-01-01_a_2024-01-31.xlsx"));
        // cuerpo
        assertArrayEquals(fakeExcel, (byte[]) resp.getEntity());

        verify(excelService, times(1)).generarExcel(any(Date.class), any(Date.class), eq(5));
    }

    @Test
    void descargarExcel_faltaInicio_retorna400() {
        Response resp = controller.descargarExcel(null, "2024-01-10", 10);
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("inicio"));
        verifyNoInteractions(excelService);
    }

    @Test
    void descargarExcel_faltaFin_retorna400() {
        Response resp = controller.descargarExcel("2024-01-01", null, 10);
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("fin"));
        verifyNoInteractions(excelService);
    }

    @Test
    void descargarExcel_formatoInvalido_retorna400() {
        Response resp = controller.descargarExcel("2024-13-01", "2024-01-10", 10);
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().toLowerCase().contains("formato"));
        verifyNoInteractions(excelService);
    }

    @Test
    void descargarExcel_rangoInvalido_retorna400() {
        Response resp = controller.descargarExcel("2024-02-01", "2024-01-01", 10);
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().toLowerCase().contains("fin"));
        verifyNoInteractions(excelService);
    }

    @Test
    void descargarExcel_limiteInvalido_retorna400() {
        Response resp = controller.descargarExcel("2024-01-01", "2024-01-10", 0);
        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().contains("mayor a 0"));
        verifyNoInteractions(excelService);
    }

    @Test
    void descargarExcel_errorServicio_retorna400() throws Exception {
        when(excelService.generarExcel(any(Date.class), any(Date.class), anyInt()))
                .thenThrow(new RuntimeException("Fallo interno"));

        Response resp = controller.descargarExcel("2024-01-01", "2024-01-10", 10);

        assertEquals(400, resp.getStatus());
        assertTrue(resp.getEntity().toString().toLowerCase().contains("error generando"));
        verify(excelService, times(1)).generarExcel(any(Date.class), any(Date.class), eq(10));
    }
}
