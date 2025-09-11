package com.unis.resource;

import com.unis.model.UserAcc;
import com.unis.service.UserAccService;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserAccResourceTest {

  @Mock
  UserAccService userAccService;

  @InjectMocks
  UserAccResource resource;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  // ---------- GET /usuarios/{id} ----------
  @Test
  void getUserById_encontrado_200_conEntity() {
    Long id = 5L;
    UserAcc user = new UserAcc();
    when(userAccService.getUserById(id)).thenReturn(Optional.of(user));

    Response r = resource.getUserById(id);

    assertEquals(200, r.getStatus());
    assertSame(user, r.getEntity());
    verify(userAccService, times(1)).getUserById(id);
  }

  @Test
  void getUserById_noEncontrado_404() {
    Long id = 5L;
    when(userAccService.getUserById(id)).thenReturn(Optional.empty());

    Response r = resource.getUserById(id);

    assertEquals(404, r.getStatus());
    assertNull(r.getEntity());
    verify(userAccService, times(1)).getUserById(id);
  }

  // ---------- PUT /usuarios/{id} (updateUser) ----------
  @Test
  void updateUser_ok_200() {
    Long id = 7L;
    UserAcc updated = new UserAcc();

    Response r = resource.updateUser(id, updated);

    assertEquals(200, r.getStatus());
    verify(userAccService, times(1)).updateUser(id, updated);
  }

  @Test
  void updateUser_error_500_conMensaje() {
    Long id = 7L;
    UserAcc updated = new UserAcc();
    doThrow(new RuntimeException("Fallo al actualizar"))
        .when(userAccService).updateUser(id, updated);

    Response r = resource.updateUser(id, updated);

    assertEquals(500, r.getStatus());
    assertEquals("Fallo al actualizar", r.getEntity());
    verify(userAccService, times(1)).updateUser(id, updated);
  }

  // ---------- PUT /usuarios/{id}/cambiar-rol (changeUserRole) ----------
  @Test
  void changeUserRole_ok_200_conMensaje() {
    Long id = 9L;
    int nuevoRol = 3;

    Response r = resource.changeUserRole(id, nuevoRol);

    assertEquals(200, r.getStatus());
    assertEquals("Rol cambiado correctamente", r.getEntity());
    verify(userAccService, times(1)).changeUserRole(id, nuevoRol);
  }

  @Test
  void changeUserRole_error_500_conMensaje() {
    Long id = 9L;
    int nuevoRol = 3;
    doThrow(new RuntimeException("No se pudo cambiar rol"))
        .when(userAccService).changeUserRole(id, nuevoRol);

    Response r = resource.changeUserRole(id, nuevoRol);

    assertEquals(500, r.getStatus());
    assertEquals("No se pudo cambiar rol", r.getEntity());
    verify(userAccService, times(1)).changeUserRole(id, nuevoRol);
  }

  // ---------- GET /usuarios/me (getCurrentUser) ----------
  @Test
  void getCurrentUser_noAutenticado_401() {
    SecurityContext sec = mock(SecurityContext.class);
    when(sec.getUserPrincipal()).thenReturn(null);

    Response r = resource.getCurrentUser(sec);

    assertEquals(401, r.getStatus());
    assertNull(r.getEntity());
  }

  @Test
  void getCurrentUser_autenticado_200_conNombreUsuario() {
    SecurityContext sec = mock(SecurityContext.class);
    Principal principal = mock(Principal.class);
    when(principal.getName()).thenReturn("andres");
    when(sec.getUserPrincipal()).thenReturn(principal);

    Response r = resource.getCurrentUser(sec);

    assertEquals(200, r.getStatus());
    @SuppressWarnings("unchecked")
    Map<String, String> body = (Map<String, String>) r.getEntity();
    assertEquals(Collections.singletonMap("nombreUsuario", "andres"), body);
  }
}
