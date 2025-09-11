package com.unis.resource;

import com.unis.model.DoctorAcc;
import com.unis.service.DoctorAccService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorAccResourceTest {

  @Mock
  DoctorAccService doctorAccService;

  @InjectMocks
  DoctorAccResource resource; // cubre el ctor por defecto

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getDoctorById_encontrado_retorna200_conEntidad() {
    Long id = 5L;
    DoctorAcc doc = new DoctorAcc();

    when(doctorAccService.getDoctorById(id)).thenReturn(Optional.of(doc));

    Response resp = resource.getDoctorById(id);

    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertSame(doc, resp.getEntity());
    verify(doctorAccService, times(1)).getDoctorById(id);
    verifyNoMoreInteractions(doctorAccService);
  }

  @Test
  void getDoctorById_noEncontrado_retorna404() {
    Long id = 99L;

    when(doctorAccService.getDoctorById(id)).thenReturn(Optional.empty());

    Response resp = resource.getDoctorById(id);

    assertEquals(Response.Status.NOT_FOUND.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity());
    verify(doctorAccService, times(1)).getDoctorById(id);
    verifyNoMoreInteractions(doctorAccService);
  }

  @Test
  void updateDoctor_delegaAlServicio_yRetorna200() {
    Long id = 7L;
    DoctorAcc body = new DoctorAcc();

    Response resp = resource.updateDoctor(id, body);

    verify(doctorAccService, times(1)).updateDoctor(same(id), same(body));
    verifyNoMoreInteractions(doctorAccService);
    assertEquals(Response.Status.OK.getStatusCode(), resp.getStatus());
    assertNull(resp.getEntity()); // el recurso retorna OK sin cuerpo
  }
}
