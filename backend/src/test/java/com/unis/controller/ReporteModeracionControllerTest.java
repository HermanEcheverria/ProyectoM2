package com.unis.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.unis.dto.ModeracionReporteDTO;
import com.unis.service.ReporteModeracionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.ws.rs.WebApplicationException;

@ExtendWith(MockitoExtension.class)
class ReporteModeracionControllerTest {

    @Mock
    ReporteModeracionService service;

    @InjectMocks
    ReporteModeracionController controller;

    @Test
    void obtener_ok_devuelveListaYVerificaLlamadaAlServicio() {
        // arrange
        ModeracionReporteDTO dto1 = mock(ModeracionReporteDTO.class);
        ModeracionReporteDTO dto2 = mock(ModeracionReporteDTO.class);
        List<ModeracionReporteDTO> esperado = Arrays.asList(dto1, dto2);

        when(service.obtenerUsuariosConRechazos(any(), any(), eq(5)))
            .thenReturn(esperado);

        // act
        List<ModeracionReporteDTO> resultado =
            controller.obtener("2025-01-01", "2025-01-31", 5);

        // assert
        assertSame(esperado, resultado, "Debe devolver exactamente la lista retornada por el servicio");
        verify(service, times(1)).obtenerUsuariosConRechazos(any(), any(), eq(5));
    }

    @Test
    void obtener_faltaInicio_lanza400() {
        WebApplicationException ex = assertThrows(WebApplicationException.class,
            () -> controller.obtener(null, "2025-01-31", 10));
        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    void obtener_faltaFin_lanza400() {
        WebApplicationException ex = assertThrows(WebApplicationException.class,
            () -> controller.obtener("2025-01-01", "   ", 10));
        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    void obtener_limiteInvalido_lanza400() {
        WebApplicationException ex = assertThrows(WebApplicationException.class,
            () -> controller.obtener("2025-01-01", "2025-01-31", 0));
        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    void obtener_formatoFechaInvalido_lanza400() {
        WebApplicationException ex = assertThrows(WebApplicationException.class,
            () -> controller.obtener("01-01-2025", "2025/01/31", 10));
        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    void obtener_rangoInvalido_inicioMayorQueFin_lanza400() {
        WebApplicationException ex = assertThrows(WebApplicationException.class,
            () -> controller.obtener("2025-02-01", "2025-01-31", 10));
        assertEquals(400, ex.getResponse().getStatus());
    }

    @Test
    void obtener_ok_listaVaciaPermitida() {
        when(service.obtenerUsuariosConRechazos(any(), any(), anyInt()))
            .thenReturn(Collections.emptyList());

        List<ModeracionReporteDTO> res = controller.obtener("2025-01-01", "2025-01-31", 10);

        assertNotNull(res);
        assertTrue(res.isEmpty());
    }
}
