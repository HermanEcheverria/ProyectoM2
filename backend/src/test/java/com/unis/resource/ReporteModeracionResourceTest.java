package com.unis.resource;

import com.unis.dto.ModeracionReporteDTO;
import com.unis.service.ReporteModeracionExcelService;
import com.unis.service.ReporteModeracionService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ReporteModeracionResourceTest {

  @Mock
  ReporteModeracionService service;

  @Mock
  ReporteModeracionExcelService excelService;

  @InjectMocks
  ReporteModeracionResource resource;

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- /api/reporte-moderacion/usuarios ----------

  @Test
  void obtenerReporteUsuarios_ok_200() throws Exception {
    // Arrange
    ModeracionReporteDTO dto = new ModeracionReporteDTO();
    when(service.obtenerUsuariosConRechazos(any(Date.class), any(Date.class), eq(5)))
        .thenReturn(List.of(dto));

    // Act
    Response r = resource.obtenerReporteUsuarios("2025-02-01", "2025-02-28", 5);

    // Assert
    assertEquals(200, r.getStatus());
    @SuppressWarnings("unchecked")
    List<ModeracionReporteDTO> body = (List<ModeracionReporteDTO>) r.getEntity();
    assertNotNull(body);
    assertEquals(1, body.size());

    verify(service).obtenerUsuariosConRechazos(any(Date.class), any(Date.class), eq(5));
  }

  @Test
  void obtenerReporteUsuarios_parametrosInvalidos_400() {
    // Act: fecha inválida y formato inválido
    Response r = resource.obtenerReporteUsuarios("2025-02-31", "xxxx", 10);

    // Assert
    assertEquals(400, r.getStatus());
    assertEquals("Parámetros inválidos", r.getEntity());
    verify(service, never()).obtenerUsuariosConRechazos(any(), any(), anyInt());
  }

  // ---------- /api/reporte-moderacion/usuarios/excel ----------

  @Test
  void descargarExcelUsuarios_ok_200_conStreamYHeaders() throws Exception {
    // Arrange
    byte[] excel = { 'P','K',3,4 }; // cualquier contenido (XLSX real también empieza con PK)
    when(excelService.generarExcel(any(Date.class), any(Date.class), eq(5)))
        .thenReturn(excel);

    // Act
    Response r = resource.descargarExcelUsuarios("2025-02-01", "2025-02-28", 5);

    // Assert
    assertEquals(200, r.getStatus());
    String cd = String.valueOf(r.getHeaders().getFirst("Content-Disposition"));
    assertTrue(cd.contains("attachment"));
    assertTrue(cd.contains("reporte_usuarios_moderacion.xlsx"));

    // Leer el StreamingOutput (esto puede lanzar IOException -> por eso el throws Exception del método)
    StreamingOutput so = (StreamingOutput) r.getEntity();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    so.write(baos);
    byte[] out = baos.toByteArray();

    assertArrayEquals(excel, out);
    verify(excelService).generarExcel(any(Date.class), any(Date.class), eq(5));
  }

}
