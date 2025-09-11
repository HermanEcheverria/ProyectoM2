package com.unis.resource;

import com.unis.model.Medicamento;
import com.unis.service.MedicamentoService;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicamentoResourceTest {

  @Mock
  MedicamentoService medicamentoService;

  @InjectMocks
  MedicamentoResource resource;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- GET /medicamentos ----------
  @Test
  void listarTodos_devuelveLista() {
    Medicamento m1 = new Medicamento();
    Medicamento m2 = new Medicamento();
    when(medicamentoService.listarTodos()).thenReturn(List.of(m1, m2));

    List<Medicamento> res = resource.listarTodos();

    assertNotNull(res);
    assertEquals(2, res.size());
    verify(medicamentoService, times(1)).listarTodos();
  }

  // ---------- GET /medicamentos/{id} ----------
  @Test
  void obtenerPorId_encontrado_200_conEntity() {
    Long id = 10L;
    Medicamento med = new Medicamento();
    when(medicamentoService.obtenerPorId(id)).thenReturn(med);

    Response r = resource.obtenerPorId(id);

    assertEquals(200, r.getStatus());
    assertSame(med, r.getEntity());
    verify(medicamentoService, times(1)).obtenerPorId(id);
  }

  @Test
  void obtenerPorId_noEncontrado_404() {
    Long id = 10L;
    when(medicamentoService.obtenerPorId(id)).thenReturn(null);

    Response r = resource.obtenerPorId(id);

    assertEquals(404, r.getStatus());
    assertNull(r.getEntity());
    verify(medicamentoService, times(1)).obtenerPorId(id);
  }

  // ---------- POST /medicamentos ----------
  @Test
  void crearMedicamento_crea_201_yDevuelveEntity() {
    Medicamento req = new Medicamento();
    Medicamento creado = new Medicamento();
    when(medicamentoService.crearMedicamento(req)).thenReturn(creado);

    Response r = resource.crearMedicamento(req);

    assertEquals(201, r.getStatus());
    assertSame(creado, r.getEntity());
    verify(medicamentoService, times(1)).crearMedicamento(req);
  }

  // ---------- PUT /medicamentos/{id} ----------
  @Test
  void actualizarMedicamento_existe_200_conEntity() {
    Long id = 7L;
    Medicamento cambios = new Medicamento();
    Medicamento actualizado = new Medicamento();
    when(medicamentoService.actualizarMedicamento(id, cambios)).thenReturn(actualizado);

    Response r = resource.actualizarMedicamento(id, cambios);

    assertEquals(200, r.getStatus());
    assertSame(actualizado, r.getEntity());
    verify(medicamentoService, times(1)).actualizarMedicamento(id, cambios);
  }

  @Test
  void actualizarMedicamento_noExiste_404() {
    Long id = 7L;
    Medicamento cambios = new Medicamento();
    when(medicamentoService.actualizarMedicamento(id, cambios)).thenReturn(null);

    Response r = resource.actualizarMedicamento(id, cambios);

    assertEquals(404, r.getStatus());
    assertNull(r.getEntity());
    verify(medicamentoService, times(1)).actualizarMedicamento(id, cambios);
  }

  // ---------- DELETE /medicamentos/{id} ----------
  @Test
  void eliminarMedicamento_existe_204_noContent() {
    Long id = 3L;
    when(medicamentoService.eliminarMedicamento(id)).thenReturn(true);

    Response r = resource.eliminarMedicamento(id);

    assertEquals(204, r.getStatus());
    assertNull(r.getEntity());
    verify(medicamentoService, times(1)).eliminarMedicamento(id);
  }

  @Test
  void eliminarMedicamento_noExiste_404() {
    Long id = 3L;
    when(medicamentoService.eliminarMedicamento(id)).thenReturn(false);

    Response r = resource.eliminarMedicamento(id);

    assertEquals(404, r.getStatus());
    assertNull(r.getEntity());
    verify(medicamentoService, times(1)).eliminarMedicamento(id);
  }
}
