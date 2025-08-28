package com.unis.resource;

import com.unis.model.Paciente;
import com.unis.service.PacienteService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PacienteResourceTest {

  @Mock
  PacienteService pacienteService;

  @InjectMocks
  PacienteResource resource;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- GET /paciente ----------
  @Test
  void obtenerTodosLosPacientes_devuelveLista() {
    Paciente p1 = new Paciente();
    Paciente p2 = new Paciente();
    when(pacienteService.getAllPacientes()).thenReturn(List.of(p1, p2));

    List<Paciente> res = resource.obtenerTodosLosPacientes();

    assertNotNull(res);
    assertEquals(2, res.size());
    verify(pacienteService, times(1)).getAllPacientes();
  }

  // ---------- GET /paciente/{id} ----------
  @Test
  void obtenerPaciente_encontrado_200() {
    Long id = 5L;
    Paciente p = new Paciente();
    when(pacienteService.getPacienteById(id)).thenReturn(Optional.of(p));

    Response r = resource.obtenerPaciente(id);

    assertEquals(200, r.getStatus());
    assertSame(p, r.getEntity());
    verify(pacienteService, times(1)).getPacienteById(id);
  }

  @Test
  void obtenerPaciente_noEncontrado_404() {
    Long id = 5L;
    when(pacienteService.getPacienteById(id)).thenReturn(Optional.empty());

    Response r = resource.obtenerPaciente(id);

    assertEquals(404, r.getStatus());
    assertNull(r.getEntity());
    verify(pacienteService, times(1)).getPacienteById(id);
  }

  // ---------- POST /paciente ----------
  @Test
  void registrarPaciente_crea_201() {
    Paciente nuevo = new Paciente();

    Response r = resource.registrarPaciente(nuevo);

    assertEquals(201, r.getStatus());
    verify(pacienteService, times(1)).registrarPaciente(nuevo);
  }

  // ---------- PUT /paciente/{id} ----------
  @Test
  void actualizarPaciente_existe_200() {
    Long id = 9L;
    Paciente cambios = new Paciente();
    when(pacienteService.actualizarPaciente(id, cambios)).thenReturn(true);

    Response r = resource.actualizarPaciente(id, cambios);

    assertEquals(200, r.getStatus());
    verify(pacienteService, times(1)).actualizarPaciente(id, cambios);
  }

  @Test
  void actualizarPaciente_noExiste_404() {
    Long id = 9L;
    Paciente cambios = new Paciente();
    when(pacienteService.actualizarPaciente(id, cambios)).thenReturn(false);

    Response r = resource.actualizarPaciente(id, cambios);

    assertEquals(404, r.getStatus());
    verify(pacienteService, times(1)).actualizarPaciente(id, cambios);
  }

  // ---------- DELETE /paciente/{id} ----------
  @Test
  void eliminarPaciente_existe_200() {
    Long id = 11L;
    when(pacienteService.eliminarPaciente(id)).thenReturn(true);

    Response r = resource.eliminarPaciente(id);

    assertEquals(200, r.getStatus());
    verify(pacienteService, times(1)).eliminarPaciente(id);
  }

  @Test
  void eliminarPaciente_noExiste_404() {
    Long id = 11L;
    when(pacienteService.eliminarPaciente(id)).thenReturn(false);

    Response r = resource.eliminarPaciente(id);

    assertEquals(404, r.getStatus());
    verify(pacienteService, times(1)).eliminarPaciente(id);
  }
}
