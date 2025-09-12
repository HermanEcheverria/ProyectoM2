package com.unis.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unis.model.Cita;
import com.unis.model.Doctor;
import com.unis.service.CitaService;
import jakarta.json.JsonObject;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CitaResourceTest {

  @Mock
  CitaService citaService;

  @InjectMocks
  CitaResource resource;

  private final ObjectMapper mapper = new ObjectMapper();

  @BeforeEach
  void init() {
    MockitoAnnotations.openMocks(this);
  }

  // -------- GET /citas --------
  @Test
  void obtenerCitas_ok() {
    Cita c = new Cita();
    when(citaService.obtenerCitas()).thenReturn(List.of(c));

    List<Cita> out = resource.obtenerCitas();

    assertEquals(1, out.size());
    assertSame(c, out.get(0));
    verify(citaService).obtenerCitas();
  }

  // -------- GET /citas/{id} --------
  @Test
  void obtenerCita_ok() {
    Cita c = new Cita();
    when(citaService.obtenerCitaPorId(5L)).thenReturn(c);

    Cita out = resource.obtenerCita(5L);

    assertSame(c, out);
    verify(citaService).obtenerCitaPorId(5L);
  }

  // -------- POST /citas (agendar) --------
  @Test
  void agendarCita_400_faltaHoraInicio() {
    Cita c = new Cita();
    c.setHoraInicio(null);
    c.setHoraFin("10:00");

    Response r = resource.agendarCita(c);

    assertEquals(400, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("hora de inicio"));
    verifyNoInteractions(citaService);
  }

  @Test
  void agendarCita_400_faltaHoraFin() {
    Cita c = new Cita();
    c.setHoraInicio("09:00");
    c.setHoraFin(null);

    Response r = resource.agendarCita(c);

    assertEquals(400, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("hora de fin"));
    verifyNoInteractions(citaService);
  }

  @Test
  void agendarCita_400_finNoPosterior() {
    Cita c = new Cita();
    c.setHoraInicio("10:00");
    c.setHoraFin("10:00"); // igual => inválido

    Response r = resource.agendarCita(c);

    assertEquals(400, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("posterior"));
    verifyNoInteractions(citaService);
  }

  @Test
  void agendarCita_201_ok() {
    Cita c = new Cita();
    c.setHoraInicio("09:00");
    c.setHoraFin("10:00");

    Response r = resource.agendarCita(c);

    assertEquals(201, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).toLowerCase().contains("éxito"));
    verify(citaService).agendarCita(c);
  }

  // -------- PUT /citas/{id} (actualizar) --------
  @Test
  void actualizarCita_200_ok() {
    Cita c = new Cita();

    Response r = resource.actualizarCita(7L, c);

    assertEquals(200, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).toString().contains("actualizada"));
    verify(citaService).actualizarCita(7L, c);
  }

  @Test
  void actualizarCita_404_illegalArgument() {
    Cita c = new Cita();
    doThrow(new IllegalArgumentException("no existe")).when(citaService).actualizarCita(8L, c);

    Response r = resource.actualizarCita(8L, c);

    assertEquals(404, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("no existe"));
  }

  // -------- PUT /citas/{id}/cancelar --------
  @Test
  void cancelarCita_200_ok() throws Exception {
    Response r = resource.cancelarCita(3L);

    assertEquals(200, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("cancelada"));
    verify(citaService).cancelarCita(3L);
  }

  @Test
void cancelarCita_404_error() {
  doThrow(new RuntimeException("no encontrada")).when(citaService).cancelarCita(4L);

  Response r = resource.cancelarCita(4L);

  assertEquals(404, r.getStatus());
  assertTrue(String.valueOf(r.getEntity()).contains("no encontrada"));
  verify(citaService).cancelarCita(4L);
}


  // -------- PUT /citas/{id}/reasignar --------
  @Test
  void reasignarDoctor_404_doctorNoEncontrado() throws Exception {
    JsonNode body = mapper.readTree("{\"idDoctor\": 55}");
    when(citaService.buscarDoctorPorId(55L)).thenReturn(null);

    Response r = resource.reasignarDoctor(9L, body);

    assertEquals(404, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("Doctor no encontrado"));
    verify(citaService).buscarDoctorPorId(55L);
    verify(citaService, never()).reasignarDoctor(anyLong(), any());
  }

  @Test
  void reasignarDoctor_200_ok() throws Exception {
    JsonNode body = mapper.readTree("{\"idDoctor\": 10}");
    Doctor d = new Doctor();
    when(citaService.buscarDoctorPorId(10L)).thenReturn(d);

    Response r = resource.reasignarDoctor(2L, body);

    assertEquals(200, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("reasignado"));
    verify(citaService).reasignarDoctor(2L, d);
  }

  @Test
  void reasignarDoctor_404_illegalArgument() throws Exception {
    JsonNode body = mapper.readTree("{\"idDoctor\": 7}");
    Doctor d = new Doctor();
    when(citaService.buscarDoctorPorId(7L)).thenReturn(d);
    doThrow(new IllegalArgumentException("cita no existe"))
        .when(citaService).reasignarDoctor(100L, d);

    Response r = resource.reasignarDoctor(100L, body);

    assertEquals(404, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("cita no existe"));
  }

  @Test
  void reasignarDoctor_400_errorGenerico() throws Exception {
    JsonNode body = mapper.readTree("{\"idDoctor\": 8}");
    Doctor d = new Doctor();
    when(citaService.buscarDoctorPorId(8L)).thenReturn(d);
    doThrow(new RuntimeException("otro")).when(citaService).reasignarDoctor(5L, d);

    Response r = resource.reasignarDoctor(5L, body);

    assertEquals(400, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("Error en la reasignación"));
  }

  // -------- PUT /citas/{id}/procesar --------
  @Test
  void procesarCita_200_ok() throws Exception {
    JsonNode body = mapper.readTree("{\"diagnostico\":\"Dx\",\"resultados\":\"OK\"}");

    Response r = resource.procesarCita(6L, body);

    assertEquals(200, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("procesada"));
    verify(citaService).procesarCitaYEnviarResultados(6L, "Dx", "OK");
  }

  @Test
  void procesarCita_404_illegalArgument() throws Exception {
    JsonNode body = mapper.readTree("{\"diagnostico\":\"Dx\",\"resultados\":\"OK\"}");
    doThrow(new IllegalArgumentException("no existe"))
        .when(citaService).procesarCitaYEnviarResultados(7L, "Dx", "OK");

    Response r = resource.procesarCita(7L, body);

    assertEquals(404, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("no existe"));
  }

  @Test
  void procesarCita_500_errorGenerico() throws Exception {
    JsonNode body = mapper.readTree("{\"diagnostico\":\"Dx\",\"resultados\":\"OK\"}");
    doThrow(new RuntimeException("boom"))
        .when(citaService).procesarCitaYEnviarResultados(8L, "Dx", "OK");

    Response r = resource.procesarCita(8L, body);

    assertEquals(500, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("Error al procesar la cita"));
  }

  // -------- POST /citas/externa --------
  @Test
  void recibirDesdeAseguradora_201_ok() {
    JsonObject dto = mock(JsonObject.class);

    Response r = resource.recibirDesdeAseguradora(dto);

    assertEquals(201, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("recibida"));
    verify(citaService).crearCitaDesdeJson(dto);
  }

  @Test
  void recibirDesdeAseguradora_400_error() {
    JsonObject dto = mock(JsonObject.class);
    doThrow(new RuntimeException("fallo")).when(citaService).crearCitaDesdeJson(dto);

    Response r = resource.recibirDesdeAseguradora(dto);

    assertEquals(400, r.getStatus());
    assertTrue(String.valueOf(r.getEntity()).contains("Error al guardar cita"));
  }
}
