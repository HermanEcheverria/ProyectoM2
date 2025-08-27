package com.unis.resource;

import com.unis.dto.ReporteAgregadoDTO;
import com.unis.dto.ReporteDetalladoDTO;
import com.unis.dto.ReporteRequest;
import com.unis.dto.ReporteResponse;
import com.unis.model.Doctor;
import com.unis.service.DoctorService;
import com.unis.service.ReporteService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReporteResourceTest {

  @Mock
  ReporteService reporteService;

  @Mock
  DoctorService doctorService;

  @InjectMocks
  ReporteResource resource;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- POST /api/reportes/consultas ----------

  @Test
  void generarReporte_retorna400_siParametrosInvalidos() {
    ReporteRequest req = new ReporteRequest();
    req.setIdDoctor(null);
    req.setFechaInicio(LocalDate.of(2025, 1, 31));
    req.setFechaFin(LocalDate.of(2025, 1, 1));
    req.setTipoReporte("AGRUPADO");

    Response resp = resource.generarReporte(req);

    assertEquals(400, resp.getStatus());
    assertEquals("Parámetros inválidos", resp.getEntity());
  }

  @Test
  void generarReporte_retorna400_siFechaInicioNull() {
    ReporteRequest req = new ReporteRequest();
    req.setIdDoctor(1L);
    req.setFechaInicio(null); // rama faltante
    req.setFechaFin(LocalDate.of(2025, 1, 31));
    req.setTipoReporte("AGRUPADO");

    Response resp = resource.generarReporte(req);

    assertEquals(400, resp.getStatus());
    assertEquals("Parámetros inválidos", resp.getEntity());
  }

  @Test
  void generarReporte_retorna400_siFechaFinNull() {
    ReporteRequest req = new ReporteRequest();
    req.setIdDoctor(1L);
    req.setFechaInicio(LocalDate.of(2025, 1, 1));
    req.setFechaFin(null); // rama faltante
    req.setTipoReporte("AGRUPADO");

    Response resp = resource.generarReporte(req);

    assertEquals(400, resp.getStatus());
    assertEquals("Parámetros inválidos", resp.getEntity());
  }

  @Test
  void generarReporte_retorna400_siFechasInvertidas_sinShortCircuit() {
    ReporteRequest req = new ReporteRequest();
    req.setIdDoctor(1L); // evita short-circuit por id null
    req.setFechaInicio(LocalDate.of(2025, 2, 2));
    req.setFechaFin(LocalDate.of(2025, 2, 1)); // invertidas
    req.setTipoReporte("DETALLADO");

    Response resp = resource.generarReporte(req);

    assertEquals(400, resp.getStatus());
    assertEquals("Parámetros inválidos", resp.getEntity());
  }

  @Test
  void generarReporte_ok_conDoctorNoEncontrado() {
    ReporteRequest req = new ReporteRequest();
    req.setUsuario("x");
    req.setIdDoctor(42L);
    req.setFechaInicio(LocalDate.of(2025, 1, 1));
    req.setFechaFin(LocalDate.of(2025, 1, 2));
    req.setTipoReporte("AGRUPADO");

    when(doctorService.getDoctorById(42L)).thenReturn(Optional.empty()); // rama optDoc.isEmpty
    when(reporteService.obtenerReporteAgregado(42L, req.getFechaInicio(), req.getFechaFin()))
        .thenReturn(List.of());

    Response resp = resource.generarReporte(req);
    assertEquals(200, resp.getStatus());

    @SuppressWarnings("unchecked")
    ReporteResponse<?> body = (ReporteResponse<?>) resp.getEntity();
    assertNotNull(body);
    assertTrue(body.getEncabezado().contains("Parámetros: Doctor ID = 42"));
    assertEquals(0, body.getDatos().size());
  }

  @Test
  void generarReporte_agrupado_ok_conDoctorPorUsuario() {
    ReporteRequest req = new ReporteRequest();
    req.setUsuario("admin");
    req.setIdDoctor(1L);
    req.setFechaInicio(LocalDate.of(2025, 1, 1));
    req.setFechaFin(LocalDate.of(2025, 1, 31));
    req.setTipoReporte("AGRUPADO");

    var mockUsuario = mock(com.unis.model.Usuario.class);
    when(mockUsuario.getNombreUsuario()).thenReturn("dr.house");

    Doctor mockDoctor = mock(Doctor.class);
    when(mockDoctor.getUsuario()).thenReturn(mockUsuario);
    when(mockDoctor.getApellido()).thenReturn(null);
    when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(mockDoctor));

    when(reporteService.obtenerReporteAgregado(1L, req.getFechaInicio(), req.getFechaFin()))
        .thenReturn(List.of());

    Response resp = resource.generarReporte(req);
    assertEquals(200, resp.getStatus());

    @SuppressWarnings("unchecked")
    ReporteResponse<?> body = (ReporteResponse<?>) resp.getEntity();
    assertNotNull(body);
    assertTrue(body.getEncabezado().contains("Usuario: admin"));
    assertTrue(body.getEncabezado().contains("Doctor: dr.house"));
    assertTrue(body.getEncabezado().contains("Tipo Reporte = AGRUPADO"));
    assertEquals(0, body.getDatos().size());
  }

  @Test
  void generarReporte_detallado_ok_conDoctorPorApellido() {
    ReporteRequest req = new ReporteRequest();
    req.setUsuario(null); // -> [Anónimo]
    req.setIdDoctor(7L);
    req.setFechaInicio(LocalDate.of(2025, 2, 1));
    req.setFechaFin(LocalDate.of(2025, 2, 28));
    req.setTipoReporte("DETALLADO");

    Doctor mockDoctor = mock(Doctor.class);
    when(mockDoctor.getUsuario()).thenReturn(null);
    when(mockDoctor.getApellido()).thenReturn("García");
    when(doctorService.getDoctorById(7L)).thenReturn(Optional.of(mockDoctor));

    when(reporteService.obtenerReporteDetallado(7L, req.getFechaInicio(), req.getFechaFin()))
        .thenReturn(List.of());

    Response resp = resource.generarReporte(req);
    assertEquals(200, resp.getStatus());

    @SuppressWarnings("unchecked")
    ReporteResponse<?> body = (ReporteResponse<?>) resp.getEntity();
    assertNotNull(body);
    assertTrue(body.getEncabezado().contains("Usuario: [Anónimo]"));
    assertTrue(body.getEncabezado().contains("Doctor: García"));
    assertTrue(body.getEncabezado().contains("Tipo Reporte = DETALLADO"));
    assertEquals(0, body.getDatos().size());
  }

  // ---------- GET /api/reportes/consultas/excel ----------

  @Test
  void descargarReporteExcel_agrupado_ok_conContenidoYHeaders() throws Exception {
    Long idDoctor = 3L;
    String fi = "2025-03-01";
    String ff = "2025-03-31";
    String tipo = "AGRUPADO";
    String usuario = "ops";

    when(doctorService.getDoctorById(idDoctor)).thenReturn(Optional.empty());

    // objeto cualquiera; la resource usa reflexión para headings/filas
    ReporteAgregadoDTO dto = mock(ReporteAgregadoDTO.class);
    when(reporteService.obtenerReporteAgregado(idDoctor, LocalDate.parse(fi), LocalDate.parse(ff)))
        .thenReturn(List.of(dto));

    Response resp = resource.descargarReporteExcel(idDoctor, fi, ff, tipo, usuario);

    assertEquals(200, resp.getStatus());
    assertTrue(String.valueOf(resp.getHeaders().getFirst("Content-Disposition")).contains("attachment"));

    StreamingOutput so = (StreamingOutput) resp.getEntity();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    so.write(baos);
    byte[] bytes = baos.toByteArray();

    // XLSX (ZIP) comienza con 'PK'
    assertTrue(bytes.length >= 2);
    assertEquals('P', bytes[0]);
    assertEquals('K', bytes[1]);
  }

  @Test
  void descargarReporteExcel_detallado_ok_listaVacia_mensajeSinDatos() throws Exception {
    Long idDoctor = 9L;
    String fi = "2025-04-01";
    String ff = "2025-04-30";
    String tipo = "DETALLADO";
    String usuario = null; // -> [Anónimo]

    Doctor mockDoctor = mock(Doctor.class);
    when(mockDoctor.getUsuario()).thenReturn(null);
    when(mockDoctor.getApellido()).thenReturn(null);
    when(doctorService.getDoctorById(idDoctor)).thenReturn(Optional.of(mockDoctor));

    when(reporteService.obtenerReporteDetallado(idDoctor, LocalDate.parse(fi), LocalDate.parse(ff)))
        .thenReturn(List.of()); // vacío -> rama “No se encontraron datos...”

    Response resp = resource.descargarReporteExcel(idDoctor, fi, ff, tipo, usuario);
    assertEquals(200, resp.getStatus());

    StreamingOutput so = (StreamingOutput) resp.getEntity();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    so.write(baos);
    byte[] bytes = baos.toByteArray();

    assertTrue(bytes.length >= 2);
    assertEquals('P', bytes[0]);
    assertEquals('K', bytes[1]);
  }

  @Test
  void descargarReporteExcel_agrupado_ok_conDoctorPorUsuario() throws Exception {
    Long idDoctor = 5L;
    String fi = "2025-05-01";
    String ff = "2025-05-31";
    String tipo = "AGRUPADO";
    String usuarioQ = "build";

    var mockUsuario = mock(com.unis.model.Usuario.class);
    when(mockUsuario.getNombreUsuario()).thenReturn("dr.strange");
    Doctor mockDoctor = mock(Doctor.class);
    when(mockDoctor.getUsuario()).thenReturn(mockUsuario);
    when(mockDoctor.getApellido()).thenReturn(null);
    when(doctorService.getDoctorById(idDoctor)).thenReturn(Optional.of(mockDoctor));

    ReporteAgregadoDTO dto = mock(ReporteAgregadoDTO.class);
    when(reporteService.obtenerReporteAgregado(idDoctor, LocalDate.parse(fi), LocalDate.parse(ff)))
        .thenReturn(List.of(dto));

    Response resp = resource.descargarReporteExcel(idDoctor, fi, ff, tipo, usuarioQ);
    assertEquals(200, resp.getStatus());

    StreamingOutput so = (StreamingOutput) resp.getEntity();
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    so.write(baos);
    byte[] bytes = baos.toByteArray();
    assertTrue(bytes.length >= 2 && bytes[0] == 'P' && bytes[1] == 'K');
  }
}
