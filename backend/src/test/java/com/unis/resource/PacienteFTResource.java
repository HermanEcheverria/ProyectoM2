package com.unis.resource;

import com.unis.model.PacienteFT;
import com.unis.service.PacienteFTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacienteFTResourceTest {

  @Mock
  PacienteFTService pacienteFTService;

  @InjectMocks
  PacienteFTResource resource; // cubre el constructor por defecto

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void obtenerTodosLosPacientes_debeDevolverListaDelServicio() {
    var p1 = new PacienteFT();
    var p2 = new PacienteFT();
    var esperada = List.of(p1, p2);
    when(pacienteFTService.getAllPacientes()).thenReturn(esperada);

    var actual = resource.obtenerTodosLosPacientes();

    assertSame(esperada, actual, "La resource debe devolver exactamente la lista del servicio");
    verify(pacienteFTService, times(1)).getAllPacientes();
    verifyNoMoreInteractions(pacienteFTService);
  }

  @Test
  void obtenerTodosLosPacientes_casoListaVacia() {
    when(pacienteFTService.getAllPacientes()).thenReturn(List.of());

    var actual = resource.obtenerTodosLosPacientes();

    assertTrue(actual.isEmpty(), "Debe poder devolver lista vacía");
    verify(pacienteFTService).getAllPacientes();
    verifyNoMoreInteractions(pacienteFTService);
  }

  @Test
  void registrarPaciente_debeDelegarEnElServicio() {
    var paciente = new PacienteFT();

    resource.registrarPaciente(paciente);

    verify(pacienteFTService, times(1)).registrarPaciente(same(paciente));
    verifyNoMoreInteractions(pacienteFTService);
  }
}
