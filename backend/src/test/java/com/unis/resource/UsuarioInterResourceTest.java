package com.unis.resource;

import com.unis.model.UsuarioInter;
import com.unis.service.UsuarioInterService;
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

public class UsuarioInterResourceTest {

  @Mock
  UsuarioInterService usuarioInterService;

  @InjectMocks
  UsuarioInterResource resource;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- GET /usuariointer ----------
  @Test
  void obtenerTodos_devuelveLista() {
    UsuarioInter u1 = new UsuarioInter();
    UsuarioInter u2 = new UsuarioInter();
    when(usuarioInterService.getAllUsuarios()).thenReturn(List.of(u1, u2));

    List<UsuarioInter> res = resource.obtenerTodos();

    assertNotNull(res);
    assertEquals(2, res.size());
    verify(usuarioInterService, times(1)).getAllUsuarios();
  }

  // ---------- GET /usuariointer/{id} ----------
  @Test
  void obtenerPorId_encontrado_ok() {
    Long id = 10L;
    UsuarioInter u = new UsuarioInter();
    when(usuarioInterService.getUsuarioById(id)).thenReturn(Optional.of(u));

    Response r = resource.obtenerPorId(id);

    assertEquals(200, r.getStatus());
    assertSame(u, r.getEntity());
    verify(usuarioInterService, times(1)).getUsuarioById(id);
  }

  @Test
  void obtenerPorId_noEncontrado_404() {
    Long id = 10L;
    when(usuarioInterService.getUsuarioById(id)).thenReturn(Optional.empty());

    Response r = resource.obtenerPorId(id);

    assertEquals(404, r.getStatus());
    assertNull(r.getEntity());
    verify(usuarioInterService, times(1)).getUsuarioById(id);
  }

  // ---------- POST /usuariointer ----------
  @Test
  void crearUsuario_crea_y_retorna201() {
    UsuarioInter nuevo = new UsuarioInter();

    Response r = resource.crearUsuario(nuevo);

    assertEquals(201, r.getStatus());
    verify(usuarioInterService, times(1)).registrarUsuario(nuevo);
  }

  // ---------- PUT /usuariointer/{id} ----------
  @Test
  void actualizarUsuario_ok_200() {
    Long id = 3L;
    UsuarioInter cambios = new UsuarioInter();

    Response r = resource.actualizarUsuario(id, cambios);

    assertEquals(200, r.getStatus());
    verify(usuarioInterService, times(1)).actualizarUsuario(id, cambios);
  }

  // ---------- DELETE /usuariointer/{id} ----------
  @Test
  void eliminarUsuario_ok_200() {
    Long id = 7L;

    Response r = resource.eliminarUsuario(id);

    assertEquals(200, r.getStatus());
    verify(usuarioInterService, times(1)).eliminarUsuario(id);
  }
}
