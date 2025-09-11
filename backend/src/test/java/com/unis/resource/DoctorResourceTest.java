package com.unis.resource;

import com.unis.model.Doctor;
import com.unis.service.DoctorService;
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

public class DoctorResourceTest {

  @Mock
  DoctorService doctorService;

  @InjectMocks
  DoctorResource resource;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- GET /doctor ----------
  @Test
  void obtenerTodosLosDoctores_devuelveLista() {
    Doctor d1 = new Doctor();
    Doctor d2 = new Doctor();
    when(doctorService.getAllDoctores()).thenReturn(List.of(d1, d2));

    List<Doctor> res = resource.obtenerTodosLosDoctores();

    assertNotNull(res);
    assertEquals(2, res.size());
    verify(doctorService, times(1)).getAllDoctores();
  }

  // ---------- GET /doctor/{id} ----------
  @Test
  void obtenerDoctor_encontrado_200() {
    Long id = 5L;
    Doctor d = new Doctor();
    when(doctorService.getDoctorById(id)).thenReturn(Optional.of(d));

    Response r = resource.obtenerDoctor(id);

    assertEquals(200, r.getStatus());
    assertSame(d, r.getEntity());
    verify(doctorService, times(1)).getDoctorById(id);
  }

  @Test
  void obtenerDoctor_noEncontrado_404() {
    Long id = 5L;
    when(doctorService.getDoctorById(id)).thenReturn(Optional.empty());

    Response r = resource.obtenerDoctor(id);

    assertEquals(404, r.getStatus());
    assertNull(r.getEntity());
    verify(doctorService, times(1)).getDoctorById(id);
  }

  // ---------- POST /doctor ----------
  @Test
  void registrarDoctor_crea_201() {
    Doctor nuevo = new Doctor();

    Response r = resource.registrarDoctor(nuevo);

    assertEquals(201, r.getStatus());
    verify(doctorService, times(1)).registrarDoctor(nuevo);
  }

  // ---------- PUT /doctor/{id} ----------
  @Test
  void actualizarDoctor_existe_200() {
    Long id = 3L;
    Doctor cambios = new Doctor();
    when(doctorService.actualizarDoctor(id, cambios)).thenReturn(true);

    Response r = resource.actualizarDoctor(id, cambios);

    assertEquals(200, r.getStatus());
    verify(doctorService, times(1)).actualizarDoctor(id, cambios);
  }

  @Test
  void actualizarDoctor_noExiste_404() {
    Long id = 3L;
    Doctor cambios = new Doctor();
    when(doctorService.actualizarDoctor(id, cambios)).thenReturn(false);

    Response r = resource.actualizarDoctor(id, cambios);

    assertEquals(404, r.getStatus());
    verify(doctorService, times(1)).actualizarDoctor(id, cambios);
  }

  // ---------- DELETE /doctor/{id} ----------
  @Test
  void eliminarDoctor_existe_200() {
    Long id = 7L;
    when(doctorService.eliminarDoctor(id)).thenReturn(true);

    Response r = resource.eliminarDoctor(id);

    assertEquals(200, r.getStatus());
    verify(doctorService, times(1)).eliminarDoctor(id);
  }

  @Test
  void eliminarDoctor_noExiste_404() {
    Long id = 7L;
    when(doctorService.eliminarDoctor(id)).thenReturn(false);

    Response r = resource.eliminarDoctor(id);

    assertEquals(404, r.getStatus());
    verify(doctorService, times(1)).eliminarDoctor(id);
  }
}
