package com.unis.resource;

import com.unis.model.PacienteAcc;
import com.unis.service.PacienteAccService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacienteAccResourceTest {

  @Mock
  PacienteAccService pacienteAccService;

  @InjectMocks
  PacienteAccResource resource; // Cubre el ctor por defecto

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getPacienteById_encontrado_retorna200_conEntidad() {
    Long id = 10L;
    PacienteAcc pac = new PacienteAcc();

    when(pacienteAccService.getPacienteById(id)).thenReturn(Optional.of(pac));

    Response resp = resource.getPacienteById(id);

    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertSame(pac, resp.getEntity());
    verify(pacienteAccService, times(1)).getPacienteById(id);
    verifyNoMoreInteractions(pacienteAccService);
  }

  @Test
  void getPacienteById_noEncontrado_retorna404() {
    Long id = 77L;
    when(pacienteAccService.getPacienteById(id)).thenReturn(Optional.empty());

    Response resp = resource.getPacienteById(id);

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity());
    verify(pacienteAccService, times(1)).getPacienteById(id);
    verifyNoMoreInteractions(pacienteAccService);
  }

  @Test
  void updatePaciente_delegaAlServicio_yRetorna200() {
    Long id = 5L;
    PacienteAcc body = new PacienteAcc();

    Response resp = resource.updatePaciente(id, body);

    verify(pacienteAccService, times(1)).updatePaciente(same(id), same(body));
    verifyNoMoreInteractions(pacienteAccService);
    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity()); // responde OK sin cuerpo
  }
}
