package com.unis.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import jakarta.ws.rs.core.HttpHeaders;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.unis.dto.ReporteAgregadoDTO;
import com.unis.dto.ReporteDetalladoDTO;
import com.unis.dto.ReporteRequest;
import com.unis.model.Doctor;
import com.unis.service.DoctorService;
import com.unis.service.ReporteService;

@QuarkusTest
class ReporteResourceTest {

  @InjectMock ReporteService reporteService;
  @InjectMock DoctorService  doctorService;

  // ============ POST /api/reportes/consultas ============

  @Test
  void generarReporte_debeRetornar400_porParametrosInvalidos() {
    // idDoctor null + fechas invertidas => 400
    var req = new ReporteRequest();
    req.setUsuario(null);
    req.setIdDoctor(null);
    req.setFechaInicio(LocalDate.of(2025, 1, 31));
    req.setFechaFin(LocalDate.of(2025, 1, 1));
    req.setTipoReporte("AGRUPADO");

    given()
      .contentType(ContentType.JSON)
      .body(req)
    .when()
      .post("/api/reportes/consultas")
    .then()
      .statusCode(400)
      .body(containsString("Parámetros inválidos"));
  }

  @Test
  void generarReporte_agrupado_ok_conDoctorPorUsuario() {
    // Doctor con usuario.nombreUsuario → debe armar "Doctor: <nombreUsuario>"
    var req = new ReporteRequest();
    req.setUsuario("admin");
    req.setIdDoctor(1L);
    req.setFechaInicio(LocalDate.of(2025, 1, 1));
    req.setFechaFin(LocalDate.of(2025, 1, 31));
    req.setTipoReporte("AGRUPADO");

    // Mock doctor con nombre de usuario
    var mockUsuario = Mockito.mock(com.unis.model.Usuario.class);
    Mockito.when(mockUsuario.getNombreUsuario()).thenReturn("dr.house");

    var mockDoctor = Mockito.mock(Doctor.class);
    Mockito.when(mockDoctor.getUsuario()).thenReturn(mockUsuario);
    Mockito.when(mockDoctor.getApellido()).thenReturn(null);

    Mockito.when(doctorService.getDoctorById(1L)).thenReturn(Optional.of(mockDoctor));

    // Para JSON podemos regresar lista vacía (no afecta serialización)
    Mockito.when(reporteService.obtenerReporteAgregado(1L, req.getFechaInicio(), req.getFechaFin()))
           .thenReturn(List.of());

    given()
      .contentType(ContentType.JSON)
      .body(req)
    .when()
      .post("/api/reportes/consultas")
    .then()
      .statusCode(200)
      .body("encabezado", allOf(containsString("Usuario: admin"),
                                containsString("Doctor: dr.house"),
                                containsString("Tipo Reporte = AGRUPADO")))
      .body("datos.size()", equalTo(0));
  }

  @Test
  void generarReporte_detallado_ok_conDoctorPorApellido() {
    var req = new ReporteRequest();
    req.setUsuario(null); // debe caer en "[Anónimo]"
    req.setIdDoctor(7L);
    req.setFechaInicio(LocalDate.of(2025, 2, 1));
    req.setFechaFin(LocalDate.of(2025, 2, 28));
    req.setTipoReporte("DETALLADO");

    // Doctor sin usuario pero con apellido
    var mockDoctor = Mockito.mock(Doctor.class);
    Mockito.when(mockDoctor.getUsuario()).thenReturn(null);
    Mockito.when(mockDoctor.getApellido()).thenReturn("García");
    Mockito.when(doctorService.getDoctorById(7L)).thenReturn(Optional.of(mockDoctor));

    Mockito.when(reporteService.obtenerReporteDetallado(7L, req.getFechaInicio(), req.getFechaFin()))
           .thenReturn(List.of());

    given()
      .contentType(ContentType.JSON)
      .body(req)
    .when()
      .post("/api/reportes/consultas")
    .then()
      .statusCode(200)
      .body("encabezado", allOf(containsString("Usuario: [Anónimo]"),
                                containsString("Doctor: García"),
                                containsString("Tipo Reporte = DETALLADO")))
      .body("datos.size()", equalTo(0));
  }

  // ============ GET /api/reportes/consultas/excel ============

  @Test
  void descargarReporteExcel_agrupado_ok_conContenidoYHeaders() {
    Long idDoctor = 3L;
    String fi = "2025-03-01";
    String ff = "2025-03-31";
    String tipo = "AGRUPADO";
    String usuario = "ops";

    // Doctor no encontrado → "Doctor ID = <id>"
    Mockito.when(doctorService.getDoctorById(idDoctor)).thenReturn(Optional.empty());

    // Devolvemos una lista NO vacía para activar la rama que crea encabezados y filas
    // Usamos un mock de DTO para evitar depender de constructores reales
    var dtoMock = Mockito.mock(ReporteAgregadoDTO.class);
    Mockito.when(reporteService.obtenerReporteAgregado(idDoctor, LocalDate.parse(fi), LocalDate.parse(ff)))
           .thenReturn(List.of(dtoMock));

    byte[] bytes =
      given()
      .when()
        .get("/api/reportes/consultas/excel?idDoctor={id}&fechaInicio={fi}&fechaFin={ff}&tipoReporte={t}&usuario={u}",
              idDoctor, fi, ff, tipo, usuario)
      .then()
        .statusCode(200)
        .header(HttpHeaders.CONTENT_TYPE,
                startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .header(HttpHeaders.CONTENT_DISPOSITION, containsString("attachment"))
        .extract().asByteArray();

    // XLSX es un ZIP -> debe iniciar con 'PK'
    org.junit.jupiter.api.Assertions.assertTrue(bytes.length >= 2);
    org.junit.jupiter.api.Assertions.assertEquals('P', bytes[0]);
    org.junit.jupiter.api.Assertions.assertEquals('K', bytes[1]);
  }

  @Test
  void descargarReporteExcel_detallado_ok_listaVacia_mensajeSinDatos() {
    Long idDoctor = 9L;
    String fi = "2025-04-01";
    String ff = "2025-04-30";
    String tipo = "DETALLADO";
    String usuario = null; // debe caer en "[Anónimo]"

    // Doctor con usuario null y apellido null → "[Desconocido]"
    var mockDoctor = Mockito.mock(Doctor.class);
    Mockito.when(mockDoctor.getUsuario()).thenReturn(null);
    Mockito.when(mockDoctor.getApellido()).thenReturn(null);
    Mockito.when(doctorService.getDoctorById(idDoctor)).thenReturn(Optional.of(mockDoctor));

    // Lista vacía para activar la rama "No se encontraron datos..."
    Mockito.when(reporteService.obtenerReporteDetallado(idDoctor, LocalDate.parse(fi), LocalDate.parse(ff)))
           .thenReturn(List.of());

    byte[] bytes =
      given()
      .when()
        .get("/api/reportes/consultas/excel?idDoctor={id}&fechaInicio={fi}&fechaFin={ff}&tipoReporte={t}",
              idDoctor, fi, ff, tipo)
      .then()
        .statusCode(200)
        .header(HttpHeaders.CONTENT_TYPE,
                startsWith("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
        .extract().asByteArray();

    // También debería ser XLSX válido
    org.junit.jupiter.api.Assertions.assertTrue(bytes.length >= 2);
    org.junit.jupiter.api.Assertions.assertEquals('P', bytes[0]);
    org.junit.jupiter.api.Assertions.assertEquals('K', bytes[1]);
  }
}
