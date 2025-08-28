package com.unis.resource;

import com.unis.model.EmpleadoAcc;
import com.unis.service.EmpleadoAccService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmpleadoAccResourceTest {

  @Mock
  EmpleadoAccService empleadoAccService;

  @InjectMocks
  EmpleadoAccResource resource; // Cubre el ctor por defecto

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getEmpleadoById_encontrado_retorna200_conEntidad() {
    Long id = 10L;
    EmpleadoAcc emp = new EmpleadoAcc();

    when(empleadoAccService.getEmpleadoById(id)).thenReturn(Optional.of(emp));

    Response resp = resource.getEmpleadoById(id);

    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertSame(emp, resp.getEntity());
    verify(empleadoAccService, times(1)).getEmpleadoById(id);
    verifyNoMoreInteractions(empleadoAccService);
  }

  @Test
  void getEmpleadoById_noEncontrado_retorna404() {
    Long id = 77L;
    when(empleadoAccService.getEmpleadoById(id)).thenReturn(Optional.empty());

    Response resp = resource.getEmpleadoById(id);

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity());
    verify(empleadoAccService, times(1)).getEmpleadoById(id);
    verifyNoMoreInteractions(empleadoAccService);
  }

  @Test
  void updateEmpleado_delegaAlServicio_yRetorna200() {
    Long id = 5L;
    EmpleadoAcc body = new EmpleadoAcc();

    Response resp = resource.updateEmpleado(id, body);

    verify(empleadoAccService, times(1)).updateEmpleado(same(id), same(body));
    verifyNoMoreInteractions(empleadoAccService);
    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity()); // el recurso responde OK sin cuerpo
  }
}
